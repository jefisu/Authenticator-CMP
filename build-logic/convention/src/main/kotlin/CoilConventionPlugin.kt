import ext.commonMainImplementation
import ext.getPluginId
import ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CoilConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.getPluginId("auth-ktor"))

        dependencies {
            commonMainImplementation(libs.findLibrary("coil-compose").get())
            commonMainImplementation(libs.findLibrary("coil-test").get())
            commonMainImplementation(libs.findLibrary("coil-network-ktor").get())
        }
    }
}