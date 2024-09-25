package de.infolektuell.gradle.bass.extensions

import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileTree
import org.gradle.api.file.RegularFile
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Provider
import javax.inject.Inject

abstract class LayoutHandler @Inject constructor(objects: ObjectFactory) {
    val rootDirectory: DirectoryProperty = objects.directoryProperty()
    val include: Provider<Directory> = rootDirectory.dir("include")
    val lib: Provider<Directory> = rootDirectory.dir("lib")
    fun header(name: String): Provider<RegularFile> = include.map { it.file(name) }
}
