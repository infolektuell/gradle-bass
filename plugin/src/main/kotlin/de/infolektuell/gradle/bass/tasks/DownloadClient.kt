package de.infolektuell.gradle.bass.tasks

import org.gradle.api.GradleException
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import java.io.InputStream
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers.ofInputStream
import java.time.Duration

abstract class DownloadClient : BuildService<BuildServiceParameters.None> {
    private val client: HttpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build()

    fun download(source: URI): InputStream {
        val request: HttpRequest = HttpRequest.newBuilder().uri(source).build()
        val response = client.send(request, ofInputStream())
        if (response.statusCode() != 200) throw GradleException("Downloading from $source failed with status code ${response.statusCode()}.")
        return response.body()
    }
    companion object {
        const val SERVICE_NAME = "bassDownloadClient"
    }
}
