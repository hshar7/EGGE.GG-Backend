package com.hshar.eggegg.graphql.users

data class NewUserInput (
    val name: String,
    val email: String,
    val password: String,
    val publicAddress: String,
    val username: String
)
