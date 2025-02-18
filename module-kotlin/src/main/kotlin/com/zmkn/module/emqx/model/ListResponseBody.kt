package com.zmkn.module.emqx.model

import kotlinx.serialization.Serializable

@Serializable
data class ListResponseBody<D, M>(
    // 全局唯一的一个消息 ID
    val data: List<D>,
    // 页码信息
    val meta: M,
)
