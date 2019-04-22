package com.hshar.eggegg.repository

import com.hshar.eggegg.model.Notification
import com.hshar.eggegg.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface NotificationRepository : MongoRepository<Notification, String> {
    fun findByUser(user: User): List<Notification>
}
