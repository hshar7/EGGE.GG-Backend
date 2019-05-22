package com.hshar.eggegg.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.context.event.SimpleApplicationEventMulticaster
import org.springframework.context.event.ApplicationEventMulticaster

// This makes event subscriber asynchronous

@Configuration
class AsynchronousSpringEventsConfig {
    @Bean(name = ["applicationEventMulticaster"])
    fun simpleApplicationEventMulticaster(): ApplicationEventMulticaster {
        val eventMulticaster = SimpleApplicationEventMulticaster()

        eventMulticaster.setTaskExecutor(SimpleAsyncTaskExecutor())
        return eventMulticaster
    }
}
