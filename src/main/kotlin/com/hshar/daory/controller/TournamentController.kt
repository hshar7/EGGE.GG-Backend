package com.hshar.daory.controller

import com.hshar.daory.exception.ResourceNotFoundException
import com.hshar.daory.repository.TournamentRepository
import com.hshar.daory.service.S3AwsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api")
class TournamentController {
    @Autowired
    lateinit var tournamentRepository: TournamentRepository

    @Autowired
    lateinit var s3AwsService: S3AwsService

    @PostMapping("/tournament/{tournamentId}/coverImage")
    fun uploadFile(
            @RequestParam("file") file: MultipartFile,
            @PathVariable tournamentId: String
    ): ResponseEntity<String> {
        val fullFileName = "$tournamentId/${file.originalFilename}"
        val url = "https://s3.us-east-1.amazonaws.com/eggegg-imgs/$fullFileName"

        val tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow { ResourceNotFoundException("Tournament", "id", tournamentId) }

        when (s3AwsService.putObject(fullFileName, file)) {
            true -> {
                tournament.coverImage = url
                tournamentRepository.save(tournament)
                return ResponseEntity("{\"fileUrl\": \"$url\"}", HttpStatus.OK)
            }
            false -> return ResponseEntity("{\"status\": \"failed\"}", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
