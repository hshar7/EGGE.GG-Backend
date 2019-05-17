package com.hshar.eggegg.model.permanent

import com.hshar.eggegg.model.transient.type.BracketType
import com.hshar.eggegg.model.transient.type.TournamentFormat
import com.hshar.eggegg.model.transient.type.TournamentStatus
import com.hshar.eggegg.model.transient.type.TournamentType
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.util.*

interface AbstractTournament {
    val id: String
    val matches: ArrayList<Match>
}

@Document(collection="tournaments")
data class Tournament (
        override val id: String,
        @Indexed(unique = true) val tournamentId: Int,
        val tournamentType: TournamentType,
        val bracketType: BracketType,
        val tournamentFormat: TournamentFormat,
        var tournamentStatus: TournamentStatus,
        var deadline: Date,
        @DBRef val token: Token,
        var name: String,
        var description: String,
        var prize: BigDecimal,
        var buyInFee: BigDecimal = 0.toBigDecimal(),
        @DBRef val owner: User,
        val maxPlayers: Int,
        val prizeDistribution: ArrayList<Int>,
        var winners: List<String>,
        @DBRef var game: Game,
        @DBRef val participants: ArrayList<User>,
        @DBRef override val matches: ArrayList<Match>,
        var featured: Boolean = false,
        var coverImage: String = "https://s3.us-east-2.amazonaws.com/eggegg-images/cover.jpg",
        @CreatedDate val createdAt: Date,
        @LastModifiedDate var updatedAt: Date
) : AbstractTournament