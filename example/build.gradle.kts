import de.infolektuell.gradle.bass.tasks.ExtractBass
plugins {
    id("application")
    id("de.infolektuell.jextract") version "0.1.0"
    id("de.infolektuell.bass")
}

bass.libraries {
    // register("bassflac")
    register("bassmix")
    register("basswv")
}

jextract.libraries {
    create("bass") {
        header = tasks.withType(ExtractBass::class).first().headers.file("bass.h")
        targetPackage = "com.un4seen.bass"
        headerClassName = "Bass"
        libraries = listOf("bass")
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(22)
    }
}
