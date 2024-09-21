package de.infolektuell.gradle.bass.tasks
import org.gradle.api.DefaultTask
import org.gradle.api.file.ArchiveOperations
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.provider.Property
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
    @get:Input
    abstract val headers: Property<String>
    @get:Input
    abstract val libs: Property<String>
    @get:OutputDirectory
    abstract val natives: DirectoryProperty
    @TaskAction
    fun extract(inputChanges: InputChanges) {
        inputChanges.getFileChanges(archives).forEach { fileChange ->
            if (fileChange.changeType == ChangeType.REMOVED) {
                val prefix = fileChange.file.name.takeWhile { it.isLetter() }
                fileSystem.delete { spec ->
                    spec.delete(natives.asFileTree.filter { it.name.contains("$prefix.") })
                }
                return@forEach
            }
            val tree = archiveOperations.zipTree(fileChange.file)
            fileSystem.copy { spec ->
                spec.into(natives)
                spec.into(headers) { child ->
                    child.from(tree, tree.matching { it.include("c/*.h") }.files)
                        child.include("*.h")
                }
                spec.into(libs) { child ->
                    child.from(tree.matching { it.include("*.dylib", "*.so") }.files)
                    child.from(tree.matching { it.include("x64/*.dll") }.files)
                    child.from(tree.matching { it.include("c/x64/*.lib") }.files)
                    child.include("*.dylib", "*.so", "*.dll", "*.lib")
                }
            }
        }
    }
}
