package ext

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

fun CommonExtension<*, *, *, *, *, *>.configureKotlinAndroid(
    project: Project
) = with(project) {
    compileSdk = libs.getVersion("android-compileSdk").toInt()

    defaultConfig {
        minSdk = libs.getVersion("android-minSdk").toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}