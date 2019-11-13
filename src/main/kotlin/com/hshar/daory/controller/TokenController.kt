package com.hshar.daory.controller

import com.hshar.daory.model.permanent.Token
import com.hshar.daory.repository.TokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class TokenController {

    @Autowired
    lateinit var tokenRepository: TokenRepository

    @GetMapping("/tokens")
    fun getAllTokens(): List<Token> {
        return tokenRepository.findAll()
    }
}
