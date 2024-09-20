package de.infolektuell.gradle.bass.extensions
import org.gradle.api.Action
import org.gradle.api.Named
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.model.ObjectFactory
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform
import javax.inject.Inject

abstract class LibraryHandler @Inject constructor(objects: ObjectFactory) : Named {
    val resources: NamedDomainObjectContainer<ResourceHandler> = objects.domainObjectContainer(ResourceHandler::class.java)
    fun currentResource(): NamedDomainObjectProvider<ResourceHandler> {
        val currentOs = DefaultNativePlatform.getCurrentOperatingSystem()
        return if (currentOs.isLinux) {
            resources.named("linux")
        } else if (currentOs.isMacOsX) {
            resources.named("mac")
        } else {
            resources.named("windows")
        }
    }
    fun resources(action: Action<in NamedDomainObjectContainer<ResourceHandler>>) {
        action.execute(resources)
    }
}
