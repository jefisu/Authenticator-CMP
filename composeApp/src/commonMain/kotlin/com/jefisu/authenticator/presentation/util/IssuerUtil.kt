package com.jefisu.authenticator.presentation.util

import com.jefisu.authenticator.domain.model.Issuer

val Issuer.url get() = when (this) {
    Issuer.GOOGLE -> "https://accounts.google.com"
    Issuer.FACEBOOK -> "https://facebook.com"
    Issuer.MICROSOFT -> "https://microsoft.com"
    Issuer.APPLE -> "https://apple.com"
    Issuer.TWITTER -> "https://twitter.com"
    Issuer.GITHUB -> "https://github.com"
    Issuer.AMAZON -> "https://www.amazon.com"
    Issuer.SLACK -> "https://slack.com"
    Issuer.DISCORD -> "https://discord.com"
    Issuer.YAHOO -> "https://login.yahoo.com"
    Issuer.TUMBLR -> "https://tumblr.com"
    Issuer.DROPBOX -> "https://dropbox.com"
    Issuer.BITBUCKET -> "https://bitbucket.com"
    Issuer.GITLAB -> "https://gitlab.com"
    Issuer.REDDIT -> "https://reddit.com"
    Issuer.DIGG -> "https://digg.com"
}
