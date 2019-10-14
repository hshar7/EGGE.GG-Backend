package com.hshar.eggegg.repository

import com.hshar.eggegg.model.permanent.redis.EventData
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EventDataRepository : CrudRepository<EventData, String>
