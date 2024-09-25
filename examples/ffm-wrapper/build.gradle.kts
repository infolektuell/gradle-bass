plugins {
    `java-library`
    kotlin("jvm") version "2.0.20"
    id("de.infolektuell.bass")
    id("de.infolektuell.jextract") version "0.1.0"
}

bass.libraries {
    register("bassmix")
}
jextract {
    libraries {
        create("bass") {
            header = bass.layout.header("bass.h")
            targetPackage = "com.un4seen.bass"
            headerClassName = "Bass"
            whitelist.functions.add("BASS_GetVersion")
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
    // Use the Kotlin JUnit 5 integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

val copyNatives by tasks.creating(Copy::class) {
    dependsOn(bass.layout.extractTask)
    from(fileTree(bass.layout.lib))
    include("*.dylib", "*.dll", "*.so")
    into(layout.buildDirectory.dir("libs"))
}

tasks.named<Test>("test") {
    dependsOn(copyNatives)
    useJUnitPlatform()
    jvmArgs("--enable-preview", "--enable-native-access=ALL-UNNAMED", "-Djava.library.path=${copyNatives.destinationDir.absolutePath}")
}
