plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.compose.gradle.plugin)
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
    compileOnly(libs.ktlint.gradle.plugin)
    compileOnly(libs.androidx.room.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("kotlinMultiplatform") {
            id = libs.plugins.auth.kotlinMultiplatform.get().pluginId
            implementationClass = "KMPConventionPlugin"
        }
        register("composeMultiplatform") {
            id = libs.plugins.auth.composeMultiplatform.get().pluginId
            implementationClass = "CMPConventionPlugin"
        }
        register("kotlinMultiplatformLibrary") {
            id = libs.plugins.auth.kmp.library.get().pluginId
            implementationClass = "KMPLibraryConventionPlugin"
        }
        register("composeMultiplatformLibrary") {
            id = libs.plugins.auth.cmp.library.get().pluginId
            implementationClass = "CMPLibraryConventionPlugin"
        }
        register("composeMultiplatformApplication") {
            id = libs.plugins.auth.cmp.application.get().pluginId
            implementationClass = "CMPApplicationConventionPlugin"
        }
        register("ktlint") {
            id = libs.plugins.auth.ktlint.get().pluginId
            implementationClass = "KtlintConventionPlugin"
        }
        register("room") {
            id = libs.plugins.auth.kmp.room.get().pluginId
            implementationClass = "KMPRoomConventionPlugin"
        }
        register("arrow") {
            id = libs.plugins.auth.arrow.get().pluginId
            implementationClass = "ArrowConventionPlugin"
        }
        register("kotlinSerialization") {
            id = libs.plugins.auth.kotlin.serialization.get().pluginId
            implementationClass = "KotlinSerializationConventionPlugin"
        }
        register("ktor") {
            id = libs.plugins.auth.ktor.get().pluginId
            implementationClass = "KtorConventionPlugin"
        }
        register("coil") {
            id = libs.plugins.auth.coil.get().pluginId
            implementationClass = "CoilConventionPlugin"
        }
        register("koin") {
            id = libs.plugins.auth.koin.get().pluginId
            implementationClass = "KoinConventionPlugin"
        }
    }
}

