package com.hshar.eggegg.repository

import com.hshar.eggegg.model.Match
import org.springframework.data.mongodb.repository.MongoRepository

interface MatchRepository: MongoRepository<Match, String>
