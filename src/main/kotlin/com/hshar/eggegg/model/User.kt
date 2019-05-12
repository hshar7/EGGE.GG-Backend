package com.hshar.eggegg.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import kotlin.collections.ArrayList

@Document(collection="users")
data class User(
        val id: String,
        @Indexed(unique = true) val publicAddress: String,
        var name: String = "",
        var email: String = "",
        var summary: String = "",
        @DBRef(lazy = true) var organization: AbstractOrganization?,
        @CreatedDate val createdAt: Date,
        @LastModifiedDate var updatedAt: Date
)
