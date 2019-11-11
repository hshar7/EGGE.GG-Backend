package com.hshar.eggegg.model.transient.payload

import com.hshar.eggegg.model.permanent.Role

data class JwtAuthenticationResponse (
        var accessToken: String,
        var tokenType: String = "Bearer",
        var username: String = "",
        var publicAddress: String = "",
        var userAvatar: String = "",
        var userId: String = "",
        var roles: Set<Role> = setOf()
)
