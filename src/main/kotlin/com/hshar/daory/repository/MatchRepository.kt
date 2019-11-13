package com.hshar.daory.repository

import com.hshar.daory.model.permanent.Match
import org.springframework.data.mongodb.repository.MongoRepository

interface MatchRepository: MongoRepository<Match, String>
