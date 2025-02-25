package com.jefisu.authenticator.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class Issuer(
    val identifier: String
) {
    GOOGLE("Google"),
    FACEBOOK("Facebook"),
    MICROSOFT("Microsoft"),
    APPLE("Apple"),
    TWITTER("Twitter"),
    GITHUB("GitHub"),
    AMAZON("Amazon"),
    SLACK("Slack"),
    DISCORD("Discord"),
    YAHOO("Yahoo"),
    TUMBLR("Tumblr"),
    DROPBOX("Dropbox"),
    BITBUCKET("Bitbucket"),
    GITLAB("GitLab"),
    REDDIT("Reddit"),
    DIGG("Digg");

    companion object {
        fun findByIdentifier(identifier: String) = entries.find {
            identifier.contains(it.identifier, ignoreCase = true)
        }
    }
}
