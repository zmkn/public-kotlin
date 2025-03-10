package com.zmkn.module.task.model

import kotlinx.coroutines.Job

data class Task(
    val id: String,
    val name: String,
    val desc: String? = null,
    val job: Job,
)
