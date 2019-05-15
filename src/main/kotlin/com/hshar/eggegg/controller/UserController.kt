package com.hshar.eggegg.controller

import com.hshar.eggegg.exception.ResourceNotFoundException
import com.hshar.eggegg.repository.UserRepository
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.hshar.eggegg.model.permanent.User
import com.hshar.eggegg.model.transient.payload.JwtAuthenticationResponse
import com.hshar.eggegg.repository.OrganizationRepository
import com.hshar.eggegg.security.CurrentUser
import com.hshar.eggegg.security.JwtTokenProvider
import com.hshar.eggegg.security.UserPrincipal
import com.hshar.eggegg.service.S3AwsService
import findOne
import org.apache.commons.codec.binary.Hex
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.web3j.crypto.Keys
import org.web3j.crypto.Hash
import org.web3j.crypto.Sign
import org.web3j.crypto.Sign.SignatureData
import org.web3j.utils.Numeric
import java.util.*

@RestController
@RequestMapping("/api")
class UserController {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var organizationRepository: OrganizationRepository

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var jwtTokenProvider: JwtTokenProvider

    @Autowired
    lateinit var s3AwsService: S3AwsService

    @PostMapping("/user")
    fun createOrLoginUser(@RequestBody requestBody: String): ResponseEntity<JwtAuthenticationResponse> {
        val signUpRequest = Gson().fromJson<JsonObject>(requestBody)
        if (!verifyAddressFromSignature(signUpRequest["accountAddress"].asString, signUpRequest["signature"].asString)) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }
        val user = userRepository.findByPublicAddress(signUpRequest["accountAddress"].asString)
                ?: userRepository.insert(User(
                        id = UUID.randomUUID().toString(),
                        publicAddress = signUpRequest["accountAddress"].asString,
                        organization = null,
                        createdAt = Date(),
                        updatedAt = Date()
                ))

        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        signUpRequest["accountAddress"].asString, ""
                )
        )

        SecurityContextHolder.getContext().authentication = authentication
        val jwtResponse = JwtAuthenticationResponse(jwtTokenProvider.generateToken(authentication))
        jwtResponse.userId = user.id
        jwtResponse.publicAddress = user.publicAddress
        jwtResponse.userName = user.name
        jwtResponse.userAvatar = user.avatar
        return ResponseEntity.ok(jwtResponse)
    }

    @PostMapping("/user/myAvatar")
    fun changeMyAvatar(
            @RequestParam("file") file: MultipartFile,
            @CurrentUser userPrincipal: UserPrincipal
    ): ResponseEntity<String> {
        val user = userRepository.findOne(userPrincipal.id)
                ?: throw ResourceNotFoundException("User", "id", userPrincipal.id)

        val fullFileName = "users/${userPrincipal.id}/${file.originalFilename}"
        val url = "https://s3.us-east-2.amazonaws.com/eggegg-images/$fullFileName"

        when (s3AwsService.putObject(fullFileName, file)) {
            true -> {
                user.avatar = url
                userRepository.save(user)
                return ResponseEntity("{\"fileUrl\": \"$url\"}", HttpStatus.OK)
            }
            false -> return ResponseEntity("{\"status\": \"failed\"}", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("/user/{id}/metadata")
    fun editUserMetadata(@PathVariable("id") id: String, @RequestBody requestBody: String): ResponseEntity<String> {
        val editRequest = Gson().fromJson<JsonObject>(requestBody)

        val user = userRepository.findById(id)
                .orElseThrow { ResourceNotFoundException("User", "id", id) }

        if (editRequest["name"] != null) {
            user.name = editRequest["name"].asString
        }
        if (editRequest["email"] != null) {
            user.email = editRequest["email"].asString
        }
        if (editRequest["organizationId"] != null) {
            val org = organizationRepository.findById(editRequest["organizationId"].asString)
                    .orElseThrow {
                        ResourceNotFoundException("Organization", "id", editRequest["organizationId"].asString)
                    }
            user.organization = org
        }

        return ResponseEntity(Gson().toJson(userRepository.save(user)), HttpStatus.OK)
    }

    @GetMapping("/users")
    fun getAllUsers(): ResponseEntity<String> {
        return ResponseEntity(Gson().toJson(userRepository.findAll()), HttpStatus.OK)
    }

    private fun verifyAddressFromSignature(address: String, signature: String): Boolean {
        val messageHashed = Hash.sha3(Hex.encodeHexString("hello world".toByteArray()))
        val messageHashBytes = Numeric.hexStringToByteArray(messageHashed)
        val signPrefix = ("\u0019Ethereum Signed Message:\n32").toByteArray()
        val r = signature.substring(0, 66)
        val s = signature.substring(66, 130)
        val v = "0x" + signature.substring(130, 132)

        val msgBytes = ByteArray(signPrefix.size + messageHashBytes.size)
        val prefixBytes = signPrefix

        System.arraycopy(prefixBytes, 0, msgBytes, 0, prefixBytes.size)
        System.arraycopy(messageHashBytes, 0, msgBytes, prefixBytes.size, messageHashBytes.size)

        val pubkey = Sign.signedMessageToKey(msgBytes,
                SignatureData(Numeric.hexStringToByteArray(v)[0],
                        Numeric.hexStringToByteArray(r),
                        Numeric.hexStringToByteArray(s)))
                .toString(16)

        val recoveredAddress = "0x" + Keys.getAddress(pubkey)
        return address == recoveredAddress
    }
}
