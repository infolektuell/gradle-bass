package de.infolektuell.gradle.bass.extensions
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.Nested
import javax.inject.Inject

abstract class BassExtension @Inject constructor(objects: ObjectFactory) {
    val libraries: NamedDomainObjectContainer<LibraryHandler> = objects.domainObjectContainer(LibraryHandler::class.java)
    fun libraries(action: Action<in NamedDomainObjectContainer<LibraryHandler>>) {
        action.execute(libraries)
    }
    /** The directory where the native bass files should be stored */
    abstract val natives: DirectoryProperty
    /** Auxiliary providers for accessing the bass headers and binaries after they have been downloaded and extracted */
    @get:Nested
    abstract val layout: LayoutHandler
    companion object {
        const val EXTENSION_NAME = "bass"
    }
}
