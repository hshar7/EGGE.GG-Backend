package com.hshar.eggegg

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@EnableMongoAuditing
@SpringBootApplication
class EggEggApplication

fun main(args: Array<String>) {
	runApplication<EggEggApplication>(*args)
}
