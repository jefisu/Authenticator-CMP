import com.android.build.api.dsl.ApplicationExtension
import dependencies.configureAndroidDependencies
import ext.commonMainImplementation
import ext.configureKotlinAndroid
import ext.getPluginId
import ext.getVersion
import ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile

class CMPApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.getPluginId("androidApplication"))
            apply(libs.getPluginId("auth-composeMultiplatform"))
            apply(libs.getPluginId("auth-ktlint"))
            apply(libs.getPluginId("auth-kmp-room"))
            apply(libs.getPluginId("auth-arrow"))
            apply(libs.getPluginId("auth-kotlin-serialization"))
            apply(libs.getPluginId("auth-coil"))
            apply(libs.getPluginId("auth-koin"))
        }

        extensions.configure<ApplicationExtension> {
            configureKotlinAndroid(this@with)
            configureAndroidApplication(this@with)
        }

        configureAndroidDependencies()

        dependencies {
            commonMainImplementation(libs.findLibrary("androidx-lifecycle-viewmodel").get())
            commonMainImplementation(libs.findLibrary("androidx-lifecycle-runtime-compose").get())
            commonMainImplementation(libs.findLibrary("androidx-navigation-compose").get())
        }

        configureTasks()
    }
}

fun ApplicationExtension.configureAndroidApplication(project: Project) {
    namespace = "com.jefisu.authenticator"

    defaultConfig {
        applicationId = "com.jefisu.authenticator"
        targetSdk = project.libs.getVersion("android-targetSdk").toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

private fun Project.configureTasks() {
    tasks.matching {
        val isKotlinCompileTask = it is KotlinCompile || it is KotlinNativeCompile
        val isKtlintTask = it.name.contains("runKtlint")
        isKotlinCompileTask || isKtlintTask
    }.configureEach {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}