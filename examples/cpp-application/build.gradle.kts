import de.infolektuell.gradle.bass.tasks.ExtractBass

plugins {
    `cpp-application`
    id("de.infolektuell.bass")
}

bass.libraries {
    register("bassmix")
}
val extractTask: ExtractBass = tasks.withType(ExtractBass::class).first()

application {
    dependencies {
        implementation(fileTree(extractTask.natives.dir("lib")))
    }
    privateHeaders.from(extractTask.natives.dir("include"))
}
