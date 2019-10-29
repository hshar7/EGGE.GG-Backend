package com.hshar.eggegg.model.transient.payload

data class JwtAuthenticationResponse (
        var accessToken: String,
        var tokenType: String = "Bearer",
        var username: String = "",
        var publicAddress: String = "",
        var userAvatar: String = "",
        var userId: String = ""
)
