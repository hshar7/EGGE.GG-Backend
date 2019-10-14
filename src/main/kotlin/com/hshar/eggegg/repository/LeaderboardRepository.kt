package com.hshar.eggegg.repository

import com.hshar.eggegg.model.permanent.redis.Leaderboard
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface LeaderboardRepository : CrudRepository<Leaderboard, String>
