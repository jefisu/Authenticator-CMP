package ext

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.kotlin.dsl.getByType

val Project.libs
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

val Project.moduleName get() = path
    .split(":")
    .filter { it.isNotBlank() }
    .joinToString("") { it.capitalized() }

val Project.modulePackageName get() = path
    .split(":")
    .filter { it.isNotBlank() }
    .joinToString(".") { it.lowercase() }