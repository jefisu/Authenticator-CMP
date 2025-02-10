package com.jefisu.authenticator.domain.util

import com.jefisu.authenticator.domain.model.CustomIssuer
import com.jefisu.authenticator.domain.model.Issuer

enum class DefaultIssuer(
    override val url: String,
    override val identifier: String
) : Issuer {
    GOOGLE("https://accounts.google.com/", "Google"),
    FACEBOOK("https://www.facebook.com/", "Facebook"),
    MICROSOFT("https://microsft.com/", "Microsoft"),
    APPLE("https://appleid.apple.com/", "Apple"),
    TWITTER("https://twitter.com/", "Twitter"),
    GITHUB("https://github.com/", "GitHub"),
    AMAZON("https://www.amazon.com/", "Amazon"),
    SLACK("https://slack.com/", "Slack"),
    DISCORD("https://discord.com/", "Discord"),
    YAHOO("https://login.yahoo.com/", "Yahoo"),
    TUMBLR("https://www.tumblr.com/", "Tumblr"),
    DROPBOX("https://www.dropbox.com/", "Dropbox"),
    BITBUCKET("https://bitbucket.org/", "Bitbucket"),
    GITLAB("https://gitlab.com/", "GitLab"),
    REDDIT("https://www.reddit.com/", "Reddit"),
    DIGG("https://digg.com/", "Digg");

    companion object {
        fun getIssuer(identifier: String): Issuer = entries
            .find { it.identifier.contains(identifier, ignoreCase = true) }
            ?: CustomIssuer(identifier, "")
    }
}
