plugins {
    `cpp-application`
    id("de.infolektuell.bass")
}

bass.libraries {
    register("bassmix")
}

val linkFiles: FileTree = fileTree(bass.layout.lib).matching { include("*.dylib", "*.so", "*.lib") }
val runtimeFiles: FileTree = fileTree(bass.layout.lib).matching { include("*.dylib", "*.so", "*.dll") }
application {
    privateHeaders.from(bass.layout.include)
    binaries.configureEach(CppExecutable::class) {
        linkTask.get().libs.from(linkFiles)
        installTask.get().libs = runtimeFiles
    }
}
