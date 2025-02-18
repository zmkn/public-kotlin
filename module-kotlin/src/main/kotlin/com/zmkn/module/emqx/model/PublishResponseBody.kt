package com.zmkn.module.emqx.model

import kotlinx.serialization.Serializable

@Serializable
data class PublishResponseBody(
    // 全局唯一的一个消息 ID
    val id: String,
)
