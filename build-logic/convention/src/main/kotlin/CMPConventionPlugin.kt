import dependencies.configureComposeDependencies
import ext.getPluginId
import ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project

class CMPConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.getPluginId("composeMultiplatform"))
            apply(libs.getPluginId("composeCompiler"))
            apply(libs.getPluginId("auth-kotlinMultiplatform"))
        }

        configureComposeDependencies()
    }
}