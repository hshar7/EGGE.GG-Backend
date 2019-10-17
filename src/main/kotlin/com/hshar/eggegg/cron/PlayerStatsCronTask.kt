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
            var totalPrize = 0.toFloat()
            tournamentRepository.findAll().forEach { tour ->
                var i = 1
                tour.eventDataWinners.forEach { winnerAddress ->
                    if (winnerAddress == user.publicAddress) {
                        val prizeCut = tour.prizeDistribution[i - 1]
                        val prize = Convert.fromWei(tour.prize, Convert.Unit.ETHER) * prizeCut.toBigDecimal() / 100.toBigDecimal()

                        totalPrize += prize.toFloat() * tour.token.usdPrice
                    }
                    i++
                }
            }
            if (totalPrize > 0) {
                leaderboardRepository.save(Leaderboard(
                        id = user.id,
                        userId = user.id,
                        userName = user.name,
                        avatar = user.avatar,
                        organizationName = "None", // TODO: Fix up lazy loading of DBREF
                        userPublicAddress = user.publicAddress,
                        earningsUSD = totalPrize
                ))
            }
        }
        logger.info("Updated leaderboard.")
    }
}
