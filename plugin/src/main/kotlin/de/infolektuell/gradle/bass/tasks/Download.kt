package de.infolektuell.gradle.bass.tasks

import de.infolektuell.gradle.bass.extensions.ResourceHandler
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.*
import org.gradle.workers.WorkerExecutor
import javax.inject.Inject

abstract class Download @Inject constructor(private val fileSystemOperations: FileSystemOperations, private val workerExecutor: WorkerExecutor) : DefaultTask() {
    @get:Internal
    abstract val downloadClient: Property<DownloadClient>
    @get:Input
    abstract val sources: SetProperty<ResourceHandler>
    @get:OutputDirectory
    abstract val destinationDir: DirectoryProperty
    @TaskAction
    fun download() {
        fileSystemOperations.delete { spec ->
            val sourceNames = sources.get().map { it.filename.get() }.toSet()
            spec.delete(destinationDir.asFileTree.filter { file -> !sourceNames.contains(file.name) })
        }
        val queue = workerExecutor.noIsolation()
        sources.get().forEach { resource ->
            queue.submit(DownloadAction::class.java) { param ->
                param.client.set(downloadClient)
                param.url.set(resource.uri)
                param.integrity.set(resource.integrity)
                param.targetFile.set(destinationDir.file(resource.filename))
            }
        }
        }
}
