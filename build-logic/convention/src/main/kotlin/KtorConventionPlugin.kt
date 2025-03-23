import ext.androidMainImplementation
import ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KtorConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {

        extensions.configure<KotlinMultiplatformExtension> {
            val appleMain by sourceSets.appleMain

            appleMain.dependencies {
                implementation(libs.findLibrary("ktor-client-darwin").get())
            }
        }

        dependencies {
            androidMainImplementation(libs.findLibrary("ktor-client-android").get())
        }
    }
}