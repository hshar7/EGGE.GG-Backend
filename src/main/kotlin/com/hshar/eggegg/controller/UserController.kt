package com.hshar.eggegg.controller

import com.hshar.eggegg.exception.ResourceNotFoundException
import com.hshar.eggegg.repository.UserRepository
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.apache.commons.codec.binary.Hex
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.web3j.crypto.Keys
import org.web3j.crypto.Hash
import org.web3j.crypto.Sign
import org.web3j.crypto.Sign.SignatureData
import org.web3j.utils.Numeric


@RestController
@RequestMapping("/api")
class UserController {

    @Autowired
    lateinit var userRepository: UserRepository

    @PostMapping("/user")
    fun createUser(@RequestBody requestBody: String): Boolean {
        val signUpRequest = Gson().fromJson<JsonObject>(requestBody)

        val match = verifyAddressFromSignature(signUpRequest["accountAddress"].asString, signUpRequest["signature"].asString)
//
//        val user = userRepository.findByPublicAddress(signUpRequest["accountAddress"].asString)
//            .orElseGet {
//                userRepository.insert(User(
//                    id = UUID.randomUUID().toString(),
//                    publicAddress = signUpRequest["accountAddress"].asString,
//                    createdAt = Date(),
//                    updatedAt = Date()
//                ))
//            }

        return match
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
        if (editRequest["organization"] != null) {
            user.organization = editRequest["organization"].asString
        }

        return ResponseEntity(Gson().toJson(userRepository.save(user)), HttpStatus.OK)
    }

    @GetMapping("/users")
    fun getAllUsers(): ResponseEntity<String> {
        return ResponseEntity(Gson().toJson(userRepository.findAll()), HttpStatus.OK)
    }

//    private fun verifyAddressFromSignature(address: String, signature: String): Boolean {
//        val messageHashBytes = Numeric.hexStringToByteArray(Hash.sha3("hello world"))
//        val r = signature.substring(0, 66)
//        val s = "0x" + signature.substring(66, 130)
//        val v = "0x" + signature.substring(130, 132)
//
//        val pubkey = Sign.signedMessageToKey(messageHashBytes,
//                SignatureData(Numeric.hexStringToByteArray(v)[0],
//                        Numeric.hexStringToByteArray(r),
//                        Numeric.hexStringToByteArray(s)))
//                .toString(16)
//
//        val recoveredAddress = Keys.getAddress(pubkey)
//        println("address: " + recoveredAddress)
//
//        return address == recoveredAddress
//    }
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
        println("address: " + recoveredAddress)


        return address == recoveredAddress
    }
}
