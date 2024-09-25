package de.infolektuell.gradle.bass.extensions

import de.infolektuell.gradle.bass.tasks.ExtractBass
import org.gradle.api.file.Directory
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import javax.inject.Inject

abstract class LayoutHandler @Inject constructor(objects: ObjectFactory) {
    val extractTask: Property<ExtractBass> = objects.property(ExtractBass::class.java)
    val rootDir: Provider<Directory> = extractTask.flatMap { it.natives }
    val include: Provider<Directory> = extractTask.flatMap { it.natives.dir(it.headers) }
    val lib: Provider<Directory> = extractTask.flatMap { it.natives.dir(it.libs) }
}
