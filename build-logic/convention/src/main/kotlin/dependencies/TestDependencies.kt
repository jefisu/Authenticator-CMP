package dependencies

import ext.androidTestImplementation
import ext.commonTestImplementation
import ext.debugImplementation
import ext.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.ExperimentalComposeLibrary

fun Project.configureTestDependencies() {
    val compose = extensions.getByType<ComposeExtension>().dependencies
    dependencies {
        commonTestImplementation(kotlin("test"))
        @OptIn(ExperimentalComposeLibrary::class)
        commonTestImplementation(compose.uiTest)
        commonTestImplementation(libs.findLibrary("kotlinx-coroutines-test").get())
        commonTestImplementation(libs.findLibrary("truthish").get())
        commonTestImplementation(libs.findLibrary("turbine").get())

        androidTestImplementation(libs.findLibrary("androidx-uitest-junit4").get())
        debugImplementation(libs.findLibrary("androidx-uitest-testManifest").get())
        debugImplementation(compose.uiTooling)
    }
}