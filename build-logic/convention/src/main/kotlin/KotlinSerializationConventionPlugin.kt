import ext.commonMainImplementation
import ext.getPluginId
import ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KotlinSerializationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.getPluginId("kotlinx-serialization"))

        dependencies {
            commonMainImplementation(libs.findLibrary("kotlinx-serialization-json").get())
        }
    }
}