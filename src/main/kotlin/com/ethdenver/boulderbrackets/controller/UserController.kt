package com.ethdenver.boulderbrackets.controller

import com.ethdenver.boulderbrackets.exception.ResourceNotFoundException
import com.ethdenver.boulderbrackets.model.User
import com.ethdenver.boulderbrackets.repository.UserRepository
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api")
class UserController {

    @Autowired
    lateinit var userRepository: UserRepository

    @PostMapping("/user")
    fun createUser(@RequestBody requestBody: String): ResponseEntity<String> {
        val signUpRequest = Gson().fromJson<JsonObject>(requestBody)

        if (!userRepository.existsByPublicAddress(signUpRequest["accountAddress"].asString)) {
            val user = userRepository.insert(User(
                id = UUID.randomUUID().toString(),
                publicAddress = signUpRequest["accountAddress"].asString
            ))

            return ResponseEntity(Gson().toJson(user), HttpStatus.CREATED)
        } else {
            val user = userRepository.findByPublicAddress(signUpRequest["accountAddress"].asString)
            return ResponseEntity(Gson().toJson(user), HttpStatus.OK)
        }
    }

    @PostMapping("/user/{id}/metadata")
    fun editUserMetadata(@PathVariable("id") id: String, @RequestBody requestBody: String): ResponseEntity<String> {
        val editRequest = Gson().fromJson<JsonObject>(requestBody)

        val user = userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User", "id", id) }

        if (editRequest["name"].isJsonPrimitive) {
            user.name = editRequest["name"].asString
        }
        if (editRequest["email"].isJsonPrimitive) {
            user.email = editRequest["email"].asString
        }

        return ResponseEntity(Gson().toJson(userRepository.save(user)), HttpStatus.OK)
    }
}
