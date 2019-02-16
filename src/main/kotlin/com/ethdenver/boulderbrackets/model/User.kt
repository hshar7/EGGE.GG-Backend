package com.ethdenver.boulderbrackets.model

import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection="users")
data class User(
    var id: String,
    @Indexed(unique = true)
    var publicAddress: String,
    var name: String = "",
    var email: String = ""
)
