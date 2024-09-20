package de.infolektuell.gradle.bass.extensions
import org.gradle.api.Named
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import java.net.URI

abstract class ResourceHandler : Named {
    abstract val uri: Property<URI>
    abstract val filename: Property<String>
    abstract val integrity: MapProperty<String, String>
}
