package com.hshar.eggegg.graphql.notifications

import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.hshar.eggegg.exception.ResourceNotFoundException
import com.hshar.eggegg.model.permanent.Notification
import com.hshar.eggegg.repository.NotificationRepository
import findOne
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class NotificationQuery : GraphQLQueryResolver {

    @Autowired
    lateinit var notificationRepository: NotificationRepository

    fun getNotification(id: String): Notification {
        return notificationRepository.findOne(id) ?: throw ResourceNotFoundException("Notification", "id", id)
    }

    fun getNotifications(count: Int, offset: Int, userId: String): Page<Notification> {
        val sort = Sort(Sort.Direction.DESC, "createdAt")
        return notificationRepository.findByUserId(userId, PageRequest.of(offset, count, sort))
    }
}
