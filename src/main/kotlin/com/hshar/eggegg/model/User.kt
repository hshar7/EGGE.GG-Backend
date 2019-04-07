package com.hshar.eggegg.model

import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection="users")
data class User(
    val id: String,
    @Indexed(unique = true)
    val publicAddress: String,
    var name: String = "",
    var email: String = "",
    var organization: String = ""
)
