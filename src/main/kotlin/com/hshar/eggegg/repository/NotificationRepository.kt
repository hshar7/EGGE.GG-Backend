package com.hshar.eggegg.repository

import com.hshar.eggegg.model.Notification
import com.hshar.eggegg.model.User
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.repository.MongoRepository

interface NotificationRepository : MongoRepository<Notification, String> {
    fun findByUser(user: User, sort: Sort): List<Notification>
}
