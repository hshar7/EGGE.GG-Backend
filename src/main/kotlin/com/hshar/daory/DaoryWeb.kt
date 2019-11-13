package com.hshar.daory

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.config.EnableMongoAuditing

@EnableMongoAuditing
@SpringBootApplication
class DaoryWeb

fun main(args: Array<String>) {
	runApplication<DaoryWeb>(*args)
}
