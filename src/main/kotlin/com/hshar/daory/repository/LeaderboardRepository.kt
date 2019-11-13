package com.hshar.daory.repository

import com.hshar.daory.model.permanent.redis.Leaderboard
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface LeaderboardRepository : CrudRepository<Leaderboard, String>
