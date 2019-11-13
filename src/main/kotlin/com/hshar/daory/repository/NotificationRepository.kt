package com.hshar.daory.repository

import com.hshar.daory.model.permanent.Notification
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.repository.MongoRepository

interface NotificationRepository : MongoRepository<Notification, String> {
    fun findByUserId(userId: String, pageRequest: PageRequest): Page<Notification>
}
