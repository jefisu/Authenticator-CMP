import ext.commonMainImplementation
import ext.getPluginId
import ext.kspCommonMainMetadata
import ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class ArrowConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.getPluginId("ksp"))

        extensions.configure<KotlinMultiplatformExtension> {
            val commonMain by sourceSets.commonMain

            commonMain.kotlin.srcDirs("build/generated/ksp/metadata/commonMain/kotlin")
        }

        dependencies {
            commonMainImplementation(libs.findLibrary("arrow-optics-compose").get())
            kspCommonMainMetadata(libs.findLibrary("arrow-optics-ksp").get())
        }
    }
}