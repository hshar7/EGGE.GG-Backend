package com.hshar.eggegg.model.transient.payload

import com.hshar.eggegg.model.transient.type.BracketType
import com.hshar.eggegg.model.transient.type.TournamentFormat
import com.hshar.eggegg.model.transient.type.TournamentType
import java.math.BigDecimal

data class IpfsSchema (
    val name: String,
    val tournamentType: TournamentType,
    val bracketType: BracketType,
    val tournamentFormat: TournamentFormat,
    val gameId: String,
    val description: String,
    val buyInFee: BigDecimal?
)
