package com.hshar.eggegg.model.permanent

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection="games")
data class Game (
    val id: String,
    var url: String = "https://www.soundtrack.net/img/album/noart.jpg",
    @Indexed(unique = true) val name: String,
    @CreatedDate val createdAt: Date,
    @LastModifiedDate var updatedAt: Date
)
