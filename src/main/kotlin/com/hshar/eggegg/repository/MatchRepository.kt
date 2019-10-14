package com.hshar.eggegg.repository

import com.hshar.eggegg.model.permanent.mongo.Match
import org.springframework.data.mongodb.repository.MongoRepository

interface MatchRepository: MongoRepository<Match, String>
