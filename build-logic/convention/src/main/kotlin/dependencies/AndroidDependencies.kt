package dependencies

import ext.androidMainImplementation
import ext.debugImplementation
import ext.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension

fun Project.configureAndroidDependencies() {
    val compose = extensions.getByType<ComposeExtension>().dependencies
    dependencies {
        androidMainImplementation(compose.preview)
        androidMainImplementation(project.libs.findLibrary("androidx-activityCompose").get())
        androidMainImplementation(project.libs.findLibrary("kotlinx-coroutines-android").get())
    }
}