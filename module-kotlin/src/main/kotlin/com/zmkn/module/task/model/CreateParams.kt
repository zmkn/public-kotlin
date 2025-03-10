package com.zmkn.module.task.model

data class CreateParams(
    val name: String,
    val desc: String? = null,
    val delayMillis: Long? = null,
)
