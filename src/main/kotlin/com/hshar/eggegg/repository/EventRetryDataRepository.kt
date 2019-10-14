package com.hshar.eggegg.repository

import com.hshar.eggegg.model.permanent.redis.EventRetryData
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRetryDataRepository : CrudRepository<EventRetryData, String>
