package com.zmkn.module.aliyunllm.audio.model

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestCreateVoiceException(
    @SerialName("request_id")
    @param:JsonProperty("request_id")
    val requestId: String? = null,
    // 错误码。
    val code: String,
    // 错误消息。
    override val message: String,
) : RuntimeException(message)
