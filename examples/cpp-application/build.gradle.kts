plugins {
    `cpp-application`
    id("de.infolektuell.bass")
}

bass.libraries {
    register("bassmix")
}

application {
    dependencies {
        implementation(fileTree(bass.layout.lib))
    }
    privateHeaders.from(bass.layout.include)
}
