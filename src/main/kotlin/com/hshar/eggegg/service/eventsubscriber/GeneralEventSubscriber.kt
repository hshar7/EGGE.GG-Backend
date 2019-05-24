package com.hshar.eggegg.service.eventsubscriber

import com.hshar.eggegg.model.permanent.EventData
import com.hshar.eggegg.model.permanent.EventRetryData
import com.hshar.eggegg.model.permanent.Web3Data
import com.hshar.eggegg.repository.EventDataRepository
import com.hshar.eggegg.repository.EventRetryDataRepository
import com.hshar.eggegg.repository.Web3DataRepository
import findOne
import io.reactivex.subscribers.DisposableSubscriber
import mu.KotlinLogging
import java.math.BigInteger

abstract class GeneralEventSubscriber<T> : DisposableSubscriber<T>() {

    abstract var web3DataRepository: Web3DataRepository
    abstract var eventDataRepository: EventDataRepository
    abstract var eventRetryDataRepository: EventRetryDataRepository
    abstract val eventName: String
    protected val logger = KotlinLogging.logger {}

    protected val retries = 6

    override fun onStart() {
        super.onStart()
        logger.info("Subscribed to $eventName.")
    }

    override fun onComplete() {
        logger.info("Complete $eventName.")
    }

    override fun onError(exception: Throwable) {
        logger.error(exception.localizedMessage)
    }

    protected fun proceedBlock(blockNumber: BigInteger, transactionHash: String) {
        web3DataRepository.save(
                Web3Data(id = eventName, fromBlock = blockNumber + 1.toBigInteger())
        )
        eventDataRepository.save(EventData(transactionHash))
    }

    protected fun beforeNext(transactionHash: String) {
        if (web3DataRepository.existsById(transactionHash)) {
            throw Error("Already processed.")
        }
    }

    protected fun needsRetry(transactionHash: String): Boolean {
        val retryData = eventRetryDataRepository.findOne(transactionHash)
                ?: eventRetryDataRepository.save(EventRetryData(id = transactionHash))
        retryData.retries++
        eventRetryDataRepository.save(retryData)
        return retryData.retries < retries
    }
}
