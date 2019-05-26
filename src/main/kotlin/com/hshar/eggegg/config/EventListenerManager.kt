package com.hshar.eggegg.config

import com.hshar.eggegg.service.EventListenerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

@Configuration
class EventListenerManager {

    @Autowired
    lateinit var eventListenerService: EventListenerService

    // TODO: This happens many times for each subscriber. Need a way to call disregard next calls
    fun initializeSubscribers() {
        eventListenerService.initSubscribers()
    }
}
