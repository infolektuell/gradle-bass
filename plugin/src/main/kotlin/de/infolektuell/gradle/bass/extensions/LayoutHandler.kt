package de.infolektuell.gradle.bass.extensions

import de.infolektuell.gradle.bass.tasks.ExtractBass
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFile
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import javax.inject.Inject

abstract class LayoutHandler @Inject constructor(objects: ObjectFactory) {
    val extractTask: Property<ExtractBass> = objects.property(ExtractBass::class.java)
    val rootDir: Provider<Directory> = extractTask.flatMap { it.natives }
    /** The directory containing the bass header files */
    val include: Provider<Directory> = extractTask.flatMap { it.natives.dir(it.headers) }
    /** The directory containing the bass binaries */
    val lib: Provider<Directory> = extractTask.flatMap { it.natives.dir(it.libs) }
    /** Returns a certain header file from the include directory */
    fun header(name: String): Provider<RegularFile> = include.map { it.file(name) }
}
