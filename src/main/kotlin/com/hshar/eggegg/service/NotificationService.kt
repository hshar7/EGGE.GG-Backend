package com.hshar.eggegg.service

import com.hshar.eggegg.model.Notification
import com.hshar.eggegg.repository.NotificationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service

@Service
@Controller
class NotificationService {

    @Autowired
    lateinit var template: SimpMessagingTemplate

    @Autowired
    lateinit var notificationRepository: NotificationRepository

    fun newNotification(notification: Notification) {
        notificationRepository.save(notification)
        template.convertAndSend("/topic/notification", notification.user.id)
    }
}