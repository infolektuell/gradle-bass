package de.infolektuell.gradle.bass

import de.infolektuell.gradle.bass.extensions.BassExtension
import de.infolektuell.gradle.bass.tasks.Download
import de.infolektuell.gradle.bass.tasks.DownloadClient
import de.infolektuell.gradle.bass.tasks.ExtractBass
import org.gradle.api.Plugin
import org.gradle.api.Project

class GradleBassPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        val constants = BassConstants()
        val downloadClient = project.gradle.sharedServices.registerIfAbsent("${project.name}_${DownloadClient.SERVICE_NAME}", DownloadClient::class.java)
        val extension = project.extensions.create(BassExtension.EXTENSION_NAME, BassExtension::class.java)
        extension.natives.convention(project.layout.buildDirectory.dir("bass/natives"))
        extension.libraries.configureEach { lib ->
            lib.resources { r ->
                r.register("linux")
                r.register("mac")
                r.register("windows")
            }
            lib.resources.configureEach { resource ->
                if (constants.hasPlatform(lib.name, resource.name)) {
                    constants.url(lib.name, resource.name)?.let { resource.uri.convention(it) }
                    constants.integrity(lib.name, resource.name)?.let { resource.integrity.convention(it) }
                    resource.filename.convention(resource.uri.map { it.path.replaceBeforeLast('/', "").trim('/') })
                }
            }
        }
        extension.libraries.register("bass")
        val downloadTask = project.tasks.register("downloadBass", Download::class.java) { task ->
            task.downloadClient.set(downloadClient)
            task.usesService(downloadClient)
            task.destinationDir.convention(project.layout.buildDirectory.dir("bass/downloads")).finalizeValueOnRead()
            extension.libraries.all { lib ->
                task.sources.add(lib.currentResource())
            }
            task.sources.finalizeValueOnRead()
        }
        val extractTask = project.tasks.register("extractBass", ExtractBass::class.java) { task ->
            task.archives.convention(downloadTask.flatMap { it.destinationDir }).finalizeValueOnRead()
            task.natives.convention(extension.natives).finalizeValueOnRead()
            task.headers.convention("include").finalizeValueOnRead()
            task.libs.convention("lib").finalizeValueOnRead()
        }
        extension.layout.extractTask.convention(extractTask).finalizeValueOnRead()
    }
    companion object {
        const val PLUGIN_NAME = "de.infolektuell.bass"
    }
}
