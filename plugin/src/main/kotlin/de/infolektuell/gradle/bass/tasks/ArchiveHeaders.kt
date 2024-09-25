package de.infolektuell.gradle.bass.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.ArchiveOperations
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.*
import org.gradle.api.tasks.bundling.Zip
import javax.inject.Inject

abstract class ArchiveHeaders @Inject constructor(private val fileSystemOperations: FileSystemOperations) : Zip() {
    @get:InputDirectory
    abstract val headers: DirectoryProperty
    @get:OutputFile
    abstract val archive: RegularFileProperty
    @TaskAction
    fun compress() {
    }
}
