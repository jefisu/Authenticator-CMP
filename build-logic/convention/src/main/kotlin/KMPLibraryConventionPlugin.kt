import com.android.build.gradle.LibraryExtension
import ext.configureKotlinAndroid
import ext.getPluginId
import ext.libs
import ext.modulePackageName
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class KMPLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        with(pluginManager) {
            apply(libs.getPluginId("androidLibrary"))
            apply(libs.getPluginId("auth-kotlinMultiplatform"))
        }

        extensions.configure<LibraryExtension> {
            configureKotlinAndroid(this@with)

            namespace = "com.jefisu.authenticator.$modulePackageName"
        }
    }
}