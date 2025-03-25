plugins {
    alias(libs.plugins.auth.cmp.application)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.barcode.scanning)
            implementation(libs.cryptography.provider.android)
        }
        appleMain.dependencies {
            implementation(libs.cryptography.provider.openssl3.prebuilt)
        }
        commonMain {
            dependencies {
                implementation(libs.kstore)
                implementation(libs.crypto.otp)
                implementation(libs.qr.kit)
                implementation(libs.calf.permissions)

                implementation(libs.calf.file.picker)
                implementation(libs.cryptography.core)
                implementation(libs.multiplatform.settings.no.arg)
            }
        }
    }
}
