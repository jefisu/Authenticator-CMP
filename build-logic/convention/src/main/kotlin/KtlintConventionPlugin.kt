import ext.getPluginId
import ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jlleitschuh.gradle.ktlint.KtlintExtension

class KtlintConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.getPluginId("ktlint"))

        extensions.configure<KtlintExtension> {
            verbose.set(true)
            outputToConsole.set(true)
            enableExperimentalRules.set(true)
            ignoreFailures.set(true)

            filter {
                exclude("**/build/**")
                include("**/kotlin/**")
            }
        }
    }
}