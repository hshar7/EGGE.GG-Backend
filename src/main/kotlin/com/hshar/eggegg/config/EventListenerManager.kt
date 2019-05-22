package com.hshar.eggegg.config

import com.hshar.eggegg.service.EventListenerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

@Configuration
class EventListenerManager {

    @Autowired
    lateinit var eventListenerService: EventListenerService

    fun initializeSubscribers() {
        eventListenerService.initSubscribers()
    }
}
