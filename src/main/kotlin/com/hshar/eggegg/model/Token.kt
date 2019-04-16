package com.hshar.eggegg.model

import org.springframework.data.mongodb.core.mapping.Document

@Document(collection="tokens")
data class Token (
    val id: String,
    val name: String,
    val symbol: String,
    val address: String,
    var usdPrice: Float,
    val tokenVersion: Int
)
