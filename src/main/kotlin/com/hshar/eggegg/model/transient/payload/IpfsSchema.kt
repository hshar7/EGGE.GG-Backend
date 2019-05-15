package com.hshar.eggegg.model.transient.payload

import com.hshar.eggegg.model.transient.type.BracketType
import com.hshar.eggegg.model.transient.type.TournamentFormat
import com.hshar.eggegg.model.transient.type.TournamentType

data class IpfsSchema (
    val name: String,
    val tournamentType: TournamentType,
    val bracketType: BracketType,
    val tournamentFormat: TournamentFormat,
    val gameId: String,
    val description: String
)
