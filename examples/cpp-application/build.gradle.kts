plugins {
    `cpp-application`
    // id("de.infolektuell.bass")
}

repositories {
    mavenLocal()
}

application {
    dependencies {
        // implementation(fileTree(bass.layout.lib))
        implementation("com.un4seen:bass:2.4.17-SNAPSHOT")
    }
    // privateHeaders.from(bass.layout.include)
}
