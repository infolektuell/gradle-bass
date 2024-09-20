package de.infolektuell.gradle.bass

import java.net.URI
import java.util.*

class BassConstants {
    private val libraryProps = Properties().apply {
        object {}.javaClass.getResourceAsStream("/libraries.properties")?.use { load(it) }
    }
    fun hasPlatform(lib: String, platform: String): Boolean = libraryProps.containsKey("$lib.$platform.url")
    fun url(lib: String, platform: String): URI? = libraryProps.getProperty("$lib.$platform.url")?.let { URI.create(it) }
    fun integrity(lib: String, platform: String): Map<String, String>? = libraryProps.getProperty("$lib.$platform.sha-256")?.let { mapOf("SHA-256" to it) }
}
