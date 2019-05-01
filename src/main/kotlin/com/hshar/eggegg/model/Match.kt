package com.hshar.eggegg.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection="matches")
data class Match (
        val id: String,
        @DBRef(lazy = true) val tournament: AbstractTournament,
        @DBRef var player1: User?,
        @DBRef var player2: User?,
        @DBRef var winner: User?,
        @DBRef val match1: Match?,
        @DBRef val match2: Match?,
        @CreatedDate val createdAt: Date,
        @LastModifiedDate var updatedAt: Date
)
