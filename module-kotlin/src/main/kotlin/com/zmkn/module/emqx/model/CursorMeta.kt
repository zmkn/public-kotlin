package com.zmkn.module.emqx.model

import kotlinx.serialization.Serializable

@Serializable
data class CursorMeta(
    val start: String,
    val position: String,
)
