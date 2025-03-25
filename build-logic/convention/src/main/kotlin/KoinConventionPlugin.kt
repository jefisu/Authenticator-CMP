import ext.androidMainImplementation
import ext.commonMainImplementation
import ext.commonTestImplementation
import ext.getPluginId
import ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.getPluginId("ksp"))

        dependencies {
            commonMainImplementation(libs.findLibrary("koin-core").get())
            commonMainImplementation(libs.findLibrary("koin-compose").get())
            commonMainImplementation(libs.findLibrary("koin-compose-viewmodel").get())
            commonMainImplementation(libs.findLibrary("koin-compose-viewmodel-navigation").get())

            androidMainImplementation(libs.findLibrary("koin-android").get())

            commonTestImplementation(libs.findLibrary("koin-test").get())
        }
    }
}