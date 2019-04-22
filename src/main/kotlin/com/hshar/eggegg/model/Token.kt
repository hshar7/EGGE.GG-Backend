package com.hshar.eggegg.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection="tokens")
data class Token (
        val id: String,
        val name: String,
        val symbol: String,
        val address: String,
        var usdPrice: Float,
        val tokenVersion: Int,
        @CreatedDate val createdAt: Date,
        @LastModifiedDate var updatedAt: Date
)
