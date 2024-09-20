package de.infolektuell.gradle.bass.tasks
import org.gradle.api.DefaultTask
import org.gradle.api.file.ArchiveOperations
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.tasks.*
import org.gradle.work.ChangeType
import org.gradle.work.Incremental
import org.gradle.work.InputChanges
import javax.inject.Inject

abstract class ExtractBass @Inject constructor(private val fileSystem: FileSystemOperations, private val archiveOperations: ArchiveOperations) : DefaultTask() {
    @get:Incremental
    @get:PathSensitive(PathSensitivity.NAME_ONLY)
    @get:InputDirectory
    abstract val archives: DirectoryProperty
    @get:OutputDirectory
    abstract val headers: DirectoryProperty
    @get:OutputDirectory
    abstract val binaries: DirectoryProperty
    @TaskAction
    fun extract(inputChanges: InputChanges) {
        inputChanges.getFileChanges(archives).forEach { fileChange ->
            if (fileChange.changeType == ChangeType.REMOVED) {
                val prefix = fileChange.file.name.takeWhile { it.isLetter() }
                fileSystem.delete { spec ->
                    spec.delete(headers.asFileTree.filter { it.name.contains("$prefix.") })
                }
                fileSystem.delete { spec ->
                    spec.delete(binaries.asFileTree.filter { it.name.contains("$prefix.") })
                }
                return@forEach
            }
            val tree = archiveOperations.zipTree(fileChange.file)
            fileSystem.copy { spec ->
                spec.from(tree).into(headers).include("*.h")
            }
            fileSystem.copy { spec ->
                spec.from(tree).into(binaries).include("*.dylib", "*.so", "*.dll")
            }
        }
    }
}
