package com.zmkn.module.task.model

data class CreateParams(
    val name: String,
    val delayMillis: Long? = null,
)
