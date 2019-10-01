package com.hshar.eggegg.model.transient.payload

data class NewEventMessage (
        val id: String,
        val type: String,
        val details: NewEventMessageDetails,
        val retries: Int
)

data class NewEventMessageDetails(
        val name: String,
        val filterId: String,
        val nodeName: String,
        val nonIndexedParameters: List<NewEventDetailsNonIndexedParams>,
        val transactionHash: String,
        val logIndex: Int,
        val blockNumber: Int,
        val blockHash: String,
        val address: String,
        val status: String,
        val eventSpecificationSignature: String,
        val networkName: String,
        val id: String
)

data class NewEventDetailsNonIndexedParams(
        val type: String,
        val value: String
)
