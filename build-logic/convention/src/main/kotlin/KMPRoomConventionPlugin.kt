import androidx.room.gradle.RoomExtension
import com.google.devtools.ksp.gradle.KspExtension
import ext.commonMainImplementation
import ext.getPluginId
import ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class KMPRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.getPluginId("room"))
            apply(libs.getPluginId("ksp"))
        }

        extensions.configure<KspExtension> {
            arg("room.generateKotlin", "true")
        }

        extensions.configure<RoomExtension> {
            schemaDirectory("$projectDir/schemas")
        }

        dependencies {
            commonMainImplementation(libs.findLibrary("androidx-sqlite-bundled").get())
            commonMainImplementation(libs.findLibrary("androidx-room-runtime").get())
            listOf(
                "kspAndroid",
                "kspIosSimulatorArm64",
                "kspIosArm64"
            ).forEach { platform ->
                add(platform, libs.findLibrary("androidx-room-compiler").get())
            }
        }
    }
}