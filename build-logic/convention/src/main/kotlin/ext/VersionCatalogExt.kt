package ext

import org.gradle.api.artifacts.VersionCatalog

fun VersionCatalog.getPluginId(alias: String): String {
    return findPlugin(alias).get().get().pluginId
}

fun VersionCatalog.getVersion(alias: String): String {
    return findVersion(alias).get().requiredVersion
}