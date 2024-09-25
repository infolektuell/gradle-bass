package de.infolektuell.gradle.bass

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.type.ArtifactTypeDefinition
import org.gradle.api.attributes.Usage
import org.gradle.api.attributes.Category
import org.gradle.api.component.SoftwareComponentFactory
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.language.cpp.CppBinary
import org.gradle.language.plugins.NativeBasePlugin
import org.gradle.nativeplatform.Linkage
import org.gradle.nativeplatform.OperatingSystemFamily
import org.gradle.nativeplatform.MachineArchitecture
import javax.inject.Inject

abstract class BassPublicationPlugin @Inject constructor(private val softwareComponentFactory: SoftwareComponentFactory) : Plugin<Project> {
    override fun apply(project: Project) {
        val cppApiUsage = project.objects.named(Usage::class.java, Usage.C_PLUS_PLUS_API)
        val linkUsage = project.objects.named(Usage::class.java, Usage.NATIVE_LINK)
        val runtimeUsage = project.objects.named(Usage::class.java, Usage.NATIVE_RUNTIME)
        val libraryCategory = project.objects.named(Category::class.java, Category.LIBRARY)
        val x64Arch = project.objects.named(MachineArchitecture::class.java, MachineArchitecture.X86_64)
        val macOs = project.objects.named(OperatingSystemFamily::class.java, OperatingSystemFamily.MACOS)
        project.configurations.create("cppCompile") {
            it.isCanBeConsumed = false
            it.attributes.attribute(Usage.USAGE_ATTRIBUTE, cppApiUsage)
        }
        project.configurations.create("cppLinkDebug") {
            it.isCanBeConsumed = false
            it.attributes
                .attribute(Usage.USAGE_ATTRIBUTE, linkUsage)
                .attribute(CppBinary.DEBUGGABLE_ATTRIBUTE, true)
                .attribute(CppBinary.OPTIMIZED_ATTRIBUTE, false)
        }
        project.configurations.create("cppLinkRelease") {
            it.isCanBeConsumed = false
            it.attributes
                .attribute(Usage.USAGE_ATTRIBUTE, linkUsage)
                .attribute(CppBinary.DEBUGGABLE_ATTRIBUTE, true)
                .attribute(CppBinary.OPTIMIZED_ATTRIBUTE, true)
        }
        project.configurations.create("cppRuntimeDebug") {
            it.isCanBeConsumed = false
            it.attributes
                .attribute(Usage.USAGE_ATTRIBUTE, runtimeUsage)
                .attribute(CppBinary.DEBUGGABLE_ATTRIBUTE, true)
                .attribute(CppBinary.OPTIMIZED_ATTRIBUTE, false)
        }
        project.configurations.create("cppRuntimeRelease") {
            it.isCanBeConsumed = false
            it.attributes
                .attribute(Usage.USAGE_ATTRIBUTE, runtimeUsage)
                .attribute(CppBinary.DEBUGGABLE_ATTRIBUTE, true)
                .attribute(CppBinary.OPTIMIZED_ATTRIBUTE, true)
        }
        val headers = project.configurations.create("headers") {
            it.isCanBeResolved = false
            it.attributes
                .attribute(Category.CATEGORY_ATTRIBUTE, libraryCategory)
                .attribute(Usage.USAGE_ATTRIBUTE, cppApiUsage)
                .attribute(ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE, ArtifactTypeDefinition.ZIP_TYPE)
        }
        val linkDebug = project.configurations.create("linkDebug") {
            it.isCanBeResolved = false
            it.attributes
                .attribute(Category.CATEGORY_ATTRIBUTE, libraryCategory)
                .attribute(Usage.USAGE_ATTRIBUTE, linkUsage)
                .attribute(CppBinary.DEBUGGABLE_ATTRIBUTE, true)
                .attribute(CppBinary.OPTIMIZED_ATTRIBUTE, false)
                .attribute(CppBinary.LINKAGE_ATTRIBUTE, Linkage.SHARED)
                .attribute(OperatingSystemFamily.OPERATING_SYSTEM_ATTRIBUTE, macOs)
                .attribute(MachineArchitecture.ARCHITECTURE_ATTRIBUTE, x64Arch)
        }
        val linkRelease = project.configurations.create("linkRelease") {
            it.isCanBeResolved = false
            it.attributes
                .attribute(Category.CATEGORY_ATTRIBUTE, libraryCategory)
                .attribute(Usage.USAGE_ATTRIBUTE, linkUsage)
                .attribute(CppBinary.DEBUGGABLE_ATTRIBUTE, true)
                .attribute(CppBinary.OPTIMIZED_ATTRIBUTE, true)
                .attribute(CppBinary.LINKAGE_ATTRIBUTE, Linkage.SHARED)
                .attribute(OperatingSystemFamily.OPERATING_SYSTEM_ATTRIBUTE, macOs)
                .attribute(MachineArchitecture.ARCHITECTURE_ATTRIBUTE, x64Arch)
        }
        val runtimeDebug = project.configurations.create("runtimeDebug") {
            it.isCanBeResolved = false
            it.attributes
                .attribute(Category.CATEGORY_ATTRIBUTE, libraryCategory)
                .attribute(Usage.USAGE_ATTRIBUTE, runtimeUsage)
                .attribute(CppBinary.DEBUGGABLE_ATTRIBUTE, true)
                .attribute(CppBinary.OPTIMIZED_ATTRIBUTE, false)
                .attribute(CppBinary.LINKAGE_ATTRIBUTE, Linkage.SHARED)
                .attribute(OperatingSystemFamily.OPERATING_SYSTEM_ATTRIBUTE, macOs)
                .attribute(MachineArchitecture.ARCHITECTURE_ATTRIBUTE, x64Arch)
        }
        val runtimeRelease = project.configurations.create("runtimeRelease") {
            it.isCanBeResolved = false
            it.attributes.attribute(Usage.USAGE_ATTRIBUTE, runtimeUsage)
            it.attributes
                .attribute(Usage.USAGE_ATTRIBUTE, runtimeUsage)
                .attribute(CppBinary.DEBUGGABLE_ATTRIBUTE, true)
                .attribute(CppBinary.OPTIMIZED_ATTRIBUTE, true)
                .attribute(CppBinary.LINKAGE_ATTRIBUTE, Linkage.SHARED)
                .attribute(OperatingSystemFamily.OPERATING_SYSTEM_ATTRIBUTE, macOs)
                .attribute(MachineArchitecture.ARCHITECTURE_ATTRIBUTE, x64Arch)
        }
        project.plugins.apply(NativeBasePlugin::class.java)
        val bassComponent = softwareComponentFactory.adhoc("bass")
        bassComponent.addVariantsFromConfiguration(headers) { v ->
            v.mapToMavenScope("compile")
        }
        bassComponent.addVariantsFromConfiguration(linkDebug) { v ->
            v.mapToMavenScope("compile")
        }
        bassComponent.addVariantsFromConfiguration(linkRelease) { v ->
            v.mapToMavenScope("compile")
        }
        bassComponent.addVariantsFromConfiguration(runtimeDebug) { v ->
            v.mapToMavenScope("runtime")
        }
        bassComponent.addVariantsFromConfiguration(runtimeRelease) { v ->
            v.mapToMavenScope("runtime")
        }
        project.components.add(bassComponent)
        project.plugins.apply("maven-publish")
        project.extensions.configure(PublishingExtension::class.java) { publishing ->
            publishing.publications.create("bass", MavenPublication::class.java) { pub ->
                pub.artifactId = "bass"
                pub.groupId = "com.un4seen"
                pub.version = "2.4.17-SNAPSHOT"
                pub.from(bassComponent)
            }
        }
    }
    companion object {
        const val PLUGIN_NAME = "de.infolektuell.bass-publication"
    }
}
