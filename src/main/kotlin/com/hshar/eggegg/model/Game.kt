package com.hshar.eggegg.model

import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection="games")
data class Game (
    val id: String,
    var url: String = "https://www.soundtrack.net/img/album/noart.jpg",
    @Indexed(unique = true)
    val name: String
)
