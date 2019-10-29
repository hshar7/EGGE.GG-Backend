package com.hshar.eggegg.controller

import className
import com.hshar.eggegg.exception.ResourceNotFoundException
import com.hshar.eggegg.repository.UserRepository
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.hshar.eggegg.model.permanent.User
import com.hshar.eggegg.model.transient.payload.JwtAuthenticationResponse
import com.hshar.eggegg.security.CurrentUser
import com.hshar.eggegg.security.JwtTokenProvider
import com.hshar.eggegg.security.UserPrincipal
import com.hshar.eggegg.service.S3AwsService
import findOne
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("/api")
class UserController {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var jwtTokenProvider: JwtTokenProvider

    @Autowired
    lateinit var s3AwsService: S3AwsService

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @GetMapping("/user")
    fun getUser(@RequestParam("id", required = false) userId: String?,
                @RequestParam("username", required = false) username: String?,
                @CurrentUser currentUser: UserPrincipal): User {
        if (userId != null) {
            return userRepository.findOne(userId)
                    ?: throw ResourceNotFoundException(userRepository.className(), "id", userId)
        } else if (username != null) {
            return userRepository.findByUsername(username)
                    ?: throw ResourceNotFoundException(userRepository.className(), "username", username)
        } else {
            return userRepository.findOne(currentUser.getId())
                    ?: throw ResourceNotFoundException(userRepository.className(), "id", currentUser.getId())
        }
    }

    @PostMapping("/user/login")
    fun authenticateUser(@RequestBody requestBody: String): ResponseEntity<JwtAuthenticationResponse> {
        val loginRequest = Gson().fromJson<JsonObject>(requestBody)
        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        loginRequest["username"], loginRequest["password"]
                )
        )

        SecurityContextHolder.getContext().authentication = authentication
        val jwt = jwtTokenProvider.generateToken(authentication)
        return ResponseEntity.ok(JwtAuthenticationResponse(jwt))
    }

    @PostMapping("/user/myAvatar")
    fun changeMyAvatar(
            @RequestParam("file") file: MultipartFile,
            @CurrentUser userPrincipal: UserPrincipal
    ): ResponseEntity<String> {
        val user = userRepository.findOne(userPrincipal.getId())
                ?: throw ResourceNotFoundException("User", "id", userPrincipal.getId())

        val fullFileName = "users/${userPrincipal.getId()}/${file.originalFilename}"
        val url = "https://s3.us-east-1.amazonaws.com/eggegg-imgs/$fullFileName"

        when (s3AwsService.putObject(fullFileName, file)) {
            true -> {
                user.avatar = url
                userRepository.save(user)
                return ResponseEntity("{\"fileUrl\": \"$url\"}", HttpStatus.OK)
            }
            false -> return ResponseEntity("{\"status\": \"failed\"}", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
