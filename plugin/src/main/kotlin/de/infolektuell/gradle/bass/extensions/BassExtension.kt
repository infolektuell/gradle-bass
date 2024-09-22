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
    abstract val natives: DirectoryProperty
    @get:Nested
    abstract val files: FilesHandler
    companion object {
        const val EXTENSION_NAME = "bass"
    }
}
