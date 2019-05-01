package com.hshar.eggegg.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigInteger
import java.util.*

interface AbstractTournament {
    val id: String
    val matches: ArrayList<Match>
}

@Document(collection="tournaments")
data class Tournament (
    override val id: String,
    @Indexed(unique = true) val tournamentId: Int,
    var deadline: Date,
    @DBRef val token: Token,
    var name: String,
    var description: String,
    var status: String = "NEW",
    var prize: BigInteger,
    @DBRef val owner: User,
    val maxPlayers: Int,
    val prizeDistribution: ArrayList<Int>,
    var winners: List<String>,
    @DBRef var game: Game,
    @DBRef val participants: ArrayList<User>,
    @DBRef override val matches: ArrayList<Match>,
    var featured: Boolean = false,
    @CreatedDate val createdAt: Date,
    @LastModifiedDate var updatedAt: Date
) : AbstractTournament
