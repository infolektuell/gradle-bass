plugins {
    `cpp-application`
    id("de.infolektuell.bass")
}

bass.libraries {
    register("bassmix")
}

application {
    dependencies {
        implementation(fileTree(bass.files.binaries))
    }
    privateHeaders.from(bass.files.headers)
}
