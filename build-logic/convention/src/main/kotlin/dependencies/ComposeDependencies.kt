package dependencies

import ext.commonMainImplementation
import ext.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension

fun Project.configureComposeDependencies() {
    val compose = extensions.getByType<ComposeExtension>().dependencies
    dependencies {
        commonMainImplementation(compose.runtime)
        commonMainImplementation(compose.foundation)
        commonMainImplementation(compose.material3)
        commonMainImplementation(compose.components.resources)
        commonMainImplementation(compose.components.uiToolingPreview)
        commonMainImplementation(libs.findLibrary("composables-core").get())
    }
}