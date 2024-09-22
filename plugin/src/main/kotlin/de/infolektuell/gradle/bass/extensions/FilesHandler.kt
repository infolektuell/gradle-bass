package de.infolektuell.gradle.bass.extensions

import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Provider
import javax.inject.Inject

abstract class FilesHandler @Inject constructor(objects: ObjectFactory) {
    val natives: DirectoryProperty = objects.directoryProperty()
    val headers: Provider<Directory> = natives.dir("include")
    val binaries: Provider<Directory> = natives.dir("lib")
}
