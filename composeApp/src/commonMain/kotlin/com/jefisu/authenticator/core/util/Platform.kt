package com.jefisu.authenticator.core.util

expect fun getPlatform(): Platform

enum class Platform {
    ANDROID,
    IOS
}
