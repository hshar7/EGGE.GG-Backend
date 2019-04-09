package com.hshar.eggegg.model

import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection="tournaments")
data class Tournament (
    val id: String,
    @Indexed(unique = true) val tournamentId: Int,
    var deadline: Date,
    val token: String,
    val tokenName: String,
    val tokenPrice: Float,
    val tokenVersion: Int,
    var name: String,
    var description: String,
    var status: String = "NEW",
    var prize: Int,
    @DBRef val owner: User,
    val maxPlayers: Int,
    val prizeDistribution: ArrayList<Int>,
    var winners: List<String>,
    @DBRef var game: Game,
    @DBRef val participants: ArrayList<User>,
    @DBRef val matches: org.bson.Document
)
