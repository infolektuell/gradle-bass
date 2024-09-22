import de.infolektuell.gradle.bass.tasks.ExtractBass

plugins {
    `java-library`
    kotlin("jvm") version "2.0.20"
    id("de.infolektuell.bass")
    id("de.infolektuell.jextract") version "0.1.0"
}

bass.libraries {
    register("bassmix")
}
val extractTask: ExtractBass = tasks.withType(ExtractBass::class).first()

jextract {
    libraries {
        create("bass") {
            header = extractTask.natives.file("bass.h")
            targetPackage = "com.un4seen.bass"
            headerClassName = "Bass"
            libraries.add("bass")
            useSystemLoadLibrary = true
        }
    }
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(22)
}

repositories {
    mavenCentral()
}

dependencies {
    runtimeOnly(fileTree(extractTask.natives.dir("lib")))
    // Use the Kotlin JUnit 5 integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
