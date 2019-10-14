package com.hshar.eggegg.model.permanent

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

interface AbstractOrganization {
    val id: String
    val name: String
    val owners: ArrayList<User>
}

@Document("organizations")
data class Organization (
        override val id: String,
        override var name: String,
        @DBRef(lazy = true) override val owners: ArrayList<User>,
        var description: String = "",
        @CreatedDate val createdAt: Date,
        @LastModifiedDate var updatedAt: Date
) : AbstractOrganization
