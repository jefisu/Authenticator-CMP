import ext.getPluginId
import ext.implementation
import ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CMPLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.getPluginId("auth-androidLibrary"))
            apply(libs.getPluginId("auth-composeMultiplatform"))
            apply(libs.getPluginId("auth-coil"))
        }

        dependencies {
            implementation(project(":core:presentation:ui"))
        }
    }
}