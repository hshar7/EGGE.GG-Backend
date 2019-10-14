package com.hshar.eggegg.cron

import com.hshar.eggegg.model.permanent.redis.Leaderboard
import com.hshar.eggegg.repository.LeaderboardRepository
import com.hshar.eggegg.repository.TournamentRepository
import com.hshar.eggegg.repository.UserRepository
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.web3j.utils.Convert

@Service
class PlayerStatsCronTask @Autowired constructor(
        val tournamentRepository: TournamentRepository,
        val userRepository: UserRepository,
        val leaderboardRepository: LeaderboardRepository
) {

    protected val logger = KotlinLogging.logger {}

    @Scheduled(fixedRate = 86400000)
    fun updateTokenPrices() {
        userRepository.findAll().forEach { user ->
            tournamentRepository.findAll().forEach { tour ->
                var i = 1
                tour.eventDataWinners.forEach { winnerAddress ->
                    if (winnerAddress == user.publicAddress) {
                        val prizeCut = tour.prizeDistribution[i - 1]
                        var prize = tour.prize * prizeCut.toBigDecimal() / 100.toBigDecimal()
                        if (prize > 0.toBigDecimal()) {
                            if (tour.token.tokenVersion == 0)
                                prize = Convert.fromWei(prize, Convert.Unit.ETHER)

                            val prizeUsd = prize.toFloat() * tour.token.usdPrice
                            leaderboardRepository.save(Leaderboard(
                                    id = user.id,
                                    userId = user.id,
                                    userName = user.name,
                                    avatar = user.avatar,
                                    organizationName = "None",
                                    userPublicAddress = user.publicAddress,
                                    earningsUSD = prizeUsd
                            ))
                        }
                    }
                    i++
                }
            }
        }
        logger.info("Updated leaderboard.")
    }
}
