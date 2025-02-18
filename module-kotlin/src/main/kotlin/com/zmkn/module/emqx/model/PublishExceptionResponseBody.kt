package com.zmkn.module.emqx.model

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PublishExceptionResponseBody(
    @SerialName("reason_code")
    @JsonProperty("reason_code")
    val reasonCode: Int,
    val message: String,
)
