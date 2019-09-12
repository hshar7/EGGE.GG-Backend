package com.hshar.eggegg.model.transient.payload

import com.hshar.eggegg.model.transient.type.BracketType
import com.hshar.eggegg.model.transient.type.TournamentFormat
import com.hshar.eggegg.model.transient.type.TournamentType
import java.math.BigDecimal

data class IpfsSchema (
    val name: String,
    val description: String,
    val gameId: String,
    val tournamentType: TournamentType,
    val bracketType: BracketType,
    val tournamentFormat: TournamentFormat,
    /** Battle Royale Things **/
    val rounds: Int = 3,
    val pointsDistribution: ArrayList<Int>?,
    val pointsToWin: Int = 15,
    /** Battle Royale Things **/
    val buyInFee: BigDecimal?
)
