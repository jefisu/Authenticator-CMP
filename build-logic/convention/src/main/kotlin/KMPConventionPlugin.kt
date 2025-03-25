import dependencies.configureTestDependencies
import ext.commonMainImplementation
import ext.getPluginId
import ext.libs
import ext.moduleName
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

class KMPConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.getPluginId("kotlinMultiplatform"))

        configureTestDependencies()

        extensions.configure<KotlinMultiplatformExtension> {
            androidPlatform()
            iosPlatform(moduleName)
        }

        dependencies {
            commonMainImplementation(libs.findLibrary("kotlinx-coroutines-core").get())
        }
    }
}

private fun KotlinMultiplatformExtension.androidPlatform() {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
    }
}

private fun KotlinMultiplatformExtension.iosPlatform(
    moduleName: String
) {
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = moduleName
            isStatic = true
            linkerOpts.add("-lsqlite3")
        }
    }
}