import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            linkerOpts.add("-lsqlite3")
        }
    }

    sourceSets {

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(compose.uiTooling)
            implementation(libs.androidx.activityCompose)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.android)
            implementation(libs.koin.android)
            implementation(libs.barcode.scanning)
            implementation(libs.cryptography.provider.android)
        }
        appleMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.cryptography.provider.openssl3.prebuilt)
        }
        commonMain {
            kotlin {
                srcDirs("build/generated/ksp/metadata/commonMain/kotlin")
            }
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.lifecycle.runtime.compose)
                implementation(libs.androidx.navigation.composee)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kermit)
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.koin.compose.viewmodel.navigation)
                implementation(libs.coil.compose)
                implementation(libs.coil.test)
                implementation(libs.coil.network.ktor)
                implementation(libs.kstore)
                implementation(libs.crypto.otp)
                implementation(libs.qr.kit)
                implementation(libs.calf.permissions)
                implementation(libs.composables.core)
                implementation(libs.arrow.optics.compose)
                implementation(libs.calf.file.picker)
                implementation(libs.androidx.sqlite.bundled)
                implementation(libs.androidx.room.runtime)
                implementation(libs.cryptography.core)
                implementation(libs.multiplatform.settings.no.arg)
            }
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

android {
    namespace = "com.jefisu.authenticator"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.jefisu.authenticator"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    androidTestImplementation(libs.androidx.uitest.junit4)
    debugImplementation(libs.androidx.uitest.testManifest)
    debugImplementation(compose.uiTooling)

    kspCommonMainMetadata(libs.arrow.optics.ksp)
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}

ktlint {
    verbose.set(true)
    outputToConsole.set(true)
    enableExperimentalRules.set(true)
    ignoreFailures.set(true)

    filter {
        exclude("**/build/**")
        include("**/kotlin/**")
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

tasks.build.dependsOn("kspCommonMainMetadata")
