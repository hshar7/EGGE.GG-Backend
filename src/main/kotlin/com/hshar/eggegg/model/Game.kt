package com.hshar.eggegg.model

import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection="games")
data class Game (
    val id: String,
    @Indexed(unique = true)
    val name: String
)
