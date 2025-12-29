package com.zmkn.model

data class ResponseException(
    val code: String,
    val status: Int,
    override val message: String? = null,
) : Exception(message)
