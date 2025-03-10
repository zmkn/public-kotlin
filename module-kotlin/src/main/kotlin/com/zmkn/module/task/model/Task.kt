package com.zmkn.module.task.model

import com.zmkn.module.task.enumeration.TaskStatus
import kotlinx.coroutines.Job

data class Task(
    val id: String,
    val job: Job,
    val name: String,
    val status: TaskStatus,
    val desc: String? = null,
    val result: Any? = null,
)
