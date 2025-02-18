package com.zmkn.module.emqx.model

import kotlinx.serialization.Serializable

@Serializable
data class ExceptionResponseBody(
    val code: String,
    val message: String,
)
