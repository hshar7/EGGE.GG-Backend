package com.hshar.eggegg.model.permanent

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection="users")
data class User(
        val id: String,
        @Indexed(unique = true) val publicAddress: String,
        @Indexed(unique = true) var username: String,
        @Indexed(unique = true) var email: String,
        var name: String = "",
        var password: String,
        var summary: String = "",
        var avatar: String = "https://s3.amazonaws.com/eggegg-imgs/egg.png",
        var roles: MutableSet<Role>,
        @CreatedDate val createdAt: Date,
        @LastModifiedDate var updatedAt: Date
)
