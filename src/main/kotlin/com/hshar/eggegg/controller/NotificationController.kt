package com.hshar.eggegg.controller

import com.google.gson.Gson
import com.hshar.eggegg.exception.ResourceNotFoundException
import com.hshar.eggegg.repository.NotificationRepository
import com.hshar.eggegg.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class NotificationController {

    @Autowired
    lateinit var notificationRepository: NotificationRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @GetMapping("/notifications/{userId}")
    fun getNotificationsForUser(@PathVariable("userId") userId: String): String {
        val user = userRepository.findById(userId)
                .orElseThrow{ ResourceNotFoundException("user", "userId", userId) }
        val sort = Sort(Sort.Direction.DESC, "createdAt")
        val notifications = notificationRepository.findByUser(user, sort)

        return Gson().toJson(notifications)
    }
}
