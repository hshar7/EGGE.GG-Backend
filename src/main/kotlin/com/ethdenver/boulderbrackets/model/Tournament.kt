package com.ethdenver.boulderbrackets.model

import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection="tournaments")
data class Tournament (
    val id: String,
    var name: String,
    var description: String,
    val maxPlayers: Int,
    @DBRef
    var game: Game,
    @DBRef
    @Indexed(unique = true)
    val participants: ArrayList<User>,
    @DBRef
    val matches: org.bson.Document
)
