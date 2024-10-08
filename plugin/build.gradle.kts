import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.0.20"
    signing
    id("com.gradle.plugin-publish") version "1.2.2"
}

gradlePlugin {
    website = "https://github.com/infolektuell/gradle-bass"
    vcsUrl = "https://github.com/infolektuell/gradle-bass.git"
    plugins.create("bassPlugin") {
        id = "de.infolektuell.bass"
        displayName = "Bass library download plugin"
        description = "Downloads and extracts the native Bass library and its optional plugins from the website"
        tags = listOf("Bass", "audio", "native")
        implementationClass = "de.infolektuell.gradle.bass.GradleBassPlugin"
    }
}

signing {
    // Get credentials from env variables for better CI compatibility
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(22)
    targetCompatibility = JavaVersion.VERSION_21
}
kotlin {
    compilerOptions.jvmTarget = JvmTarget.JVM_21
}

repositories {
    mavenCentral()
}

dependencies {
    // Use the Kotlin JUnit 5 integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// Add a source set for the functional test suite
val functionalTestSourceSet = sourceSets.create("functionalTest") {
}

configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])
configurations["functionalTestRuntimeOnly"].extendsFrom(configurations["testRuntimeOnly"])

// Add a task to run the functional tests
val functionalTest by tasks.registering(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
    useJUnitPlatform()
}

gradlePlugin.testSourceSets.add(functionalTestSourceSet)

tasks.named<Task>("check") {
    // Run the functional tests as part of `check`
    dependsOn(functionalTest)
}

tasks.named<Test>("test") {
    // Use JUnit Jupiter for unit tests.
    useJUnitPlatform()
}
