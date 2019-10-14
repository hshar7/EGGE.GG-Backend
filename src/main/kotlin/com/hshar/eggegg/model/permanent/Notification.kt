package com.hshar.eggegg.model.permanent

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("notifications")
data class Notification (
        val id: String,
        @CreatedDate val createdAt: Date,
        @DBRef val user: User,
        val message: String,
        val url: String
)
