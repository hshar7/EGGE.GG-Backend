package com.hshar.eggegg.cron

import com.hshar.eggegg.repository.TournamentRepository
import com.hshar.eggegg.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.web3j.utils.Convert

// TODO: Move this from printlns to redis and create graphql schema for it
@Service
class PlayerStatsCronTask @Autowired constructor(
        val tournamentRepository: TournamentRepository,
        val userRepository: UserRepository
) {
    @Scheduled(fixedRate = 86400000)
    fun updateTokenPrices() {
        userRepository.findAll().forEach { user ->
            println("User - ${user.publicAddress} - ${user.name}: ")
            tournamentRepository.findAll().forEach { tour ->
                if (tour.participants.contains(user)) {
                    println("-- Participant in ${tour.name}")
                    var i = 1
                    tour.eventDataWinners.forEach{ winnerAddress ->
                        if (winnerAddress == user.publicAddress) {
                            println("-- Earned Position $i in ${tour.name}")
                            val prizeCut = tour.prizeDistribution[i-1]
                            var prize = tour.prize * prizeCut.toBigDecimal() / 100.toBigDecimal()
                            if (tour.token.tokenVersion == 0)
                                prize = Convert.fromWei(prize, Convert.Unit.ETHER)

                            val prizeUsd = prize.toFloat() * tour.token.usdPrice
                            println("-- Earned $$prizeUsd ($prize ${tour.token.symbol}) in ${tour.name}")
                        }
                        i++
                    }
                }
            }
        }
    }
}
