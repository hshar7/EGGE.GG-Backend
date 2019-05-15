package com.hshar.eggegg.service.eventsubscriber

import com.hshar.eggegg.model.permanent.Web3Data
import com.hshar.eggegg.repository.Web3DataRepository
import io.reactivex.FlowableSubscriber
import mu.KotlinLogging
import org.reactivestreams.Subscription
import java.math.BigInteger

abstract class GeneralEventSubscriber<T> : FlowableSubscriber<T> {

    abstract var web3DataRepository: Web3DataRepository
    abstract val eventName: String
    protected val logger = KotlinLogging.logger {}

    override fun onSubscribe(subscription: Subscription) {
        logger.info("Subscribed to $eventName.")
        subscription.request(Long.MAX_VALUE)
    }

    override fun onComplete() {
        logger.info("Complete $eventName.")
    }

    override fun onError(exception: Throwable) {
        logger.error(exception.localizedMessage)
    }

    protected fun proceedBlock(blockNumber: BigInteger) {
        web3DataRepository.save(
                Web3Data(id = eventName, fromBlock = blockNumber + 1.toBigInteger())
        )
    }
}
