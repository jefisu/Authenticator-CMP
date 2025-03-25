package ext

import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler

fun DependencyHandler.implementation(dependencyNotation: Any): Dependency? =
    add("implementation", dependencyNotation)

fun DependencyHandler.kspCommonMainMetadata(dependencyNotation: Any): Dependency? =
    add("kspCommonMainMetadata", dependencyNotation)

fun DependencyHandler.commonMainImplementation(dependencyNotation: Any): Dependency? =
    add("commonMainImplementation", dependencyNotation)

fun DependencyHandler.androidMainImplementation(dependencyNotation: Any): Dependency? =
    add("androidMainImplementation", dependencyNotation)

fun DependencyHandler.commonTestImplementation(dependencyNotation: Any): Dependency? =
    add("commonTestImplementation", dependencyNotation)

fun DependencyHandler.androidTestImplementation(dependencyNotation: Any): Dependency? =
    add("androidTestImplementation", dependencyNotation)

fun DependencyHandler.debugImplementation(dependencyNotation: Any): Dependency? =
    add("debugImplementation", dependencyNotation)

