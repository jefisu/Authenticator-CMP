import ext.getPluginId
import ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class CMPLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.getPluginId("auth-androidLibrary"))
            apply(libs.getPluginId("auth-composeMultiplatform"))
            apply(libs.getPluginId("auth-kmp-library"))
            apply(libs.getPluginId("auth-coil"))
        }
    }
}