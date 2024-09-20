package de.infolektuell.gradle.bass.tasks

import org.gradle.api.GradleException
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.workers.WorkAction
import org.gradle.workers.WorkParameters
import java.io.InputStream
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.security.DigestInputStream
import java.security.MessageDigest

abstract class DownloadAction : WorkAction<DownloadAction.Parameters> {
    interface Parameters : WorkParameters {
        val client: Property<DownloadClient>
        val url: Property<URI>
        val integrity: MapProperty<String, String>
        val targetFile: RegularFileProperty
    }
    override fun execute() {
        val source = parameters.url.get()
        val integrity = parameters.integrity.get()
        val targetFile = parameters.targetFile.get()
        val targetPath = targetFile.asFile.toPath()
        if (integrity.isEmpty()) {
            download(source, targetPath)
            return
        }
        integrity["SHA-256"]?.let { download(source, targetPath, it, "SHA-256") }
            ?: integrity["SHA-512"]?.let { download(source, targetPath, it, "SHA-512") }
            ?: integrity["MD5"]?.let { download(source, targetPath, it, "MD5") }
            ?: integrity.entries.first().let { download(source, targetPath, it.value, it.key) }
    }
    private fun download(source: URI, targetPath: Path) {
        parameters.client.get().download(source).use { body ->
            Files.copy(body, targetPath, StandardCopyOption.REPLACE_EXISTING)
        }
    }
    private fun download(source: URI, targetPath: Path, checksum: String, algorithm: String = "SHA-256") {
        if (verify(targetPath, checksum, algorithm)) return
        val body = parameters.client.get().download(source)
        copyVerified(body, targetPath, checksum, algorithm)
    }
    @OptIn(ExperimentalStdlibApi::class)
    private fun verify(file: Path, checksum: String, algorithm: String = "SHA-256"): Boolean {
        return Files.exists(file) && DigestInputStream(Files.newInputStream(file), MessageDigest.getInstance(algorithm)).use { input ->
            input.readAllBytes()
            input.messageDigest.digest().toHexString() == checksum
        }
    }
    @OptIn(ExperimentalStdlibApi::class)
    private fun copyVerified(body: InputStream, target: Path, checksum: String, algorithm: String = "SHA-256") {
        val inputChecksum = DigestInputStream(body, MessageDigest.getInstance(algorithm)).use { input ->
            Files.copy(input, target, StandardCopyOption.REPLACE_EXISTING)
            input.messageDigest.digest().toHexString()
        }
        if (inputChecksum != checksum) {
            Files.deleteIfExists(target)
            throw GradleException("Input checksum $inputChecksum does not match expected checksum $checksum")
        }
    }
}
