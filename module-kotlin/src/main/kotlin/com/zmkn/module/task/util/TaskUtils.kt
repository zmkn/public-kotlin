package com.zmkn.module.task.util

import com.zmkn.module.task.Task

object TaskUtils {
    val jobs = Task.jobs

    fun getTask(id: String) = Task.getTask(id)

    fun create(
        name: String,
        desc: String? = null,
        delayMillis: Long? = null,
        block: suspend (task: Task) -> Any?
    ) = Task.create(
        name = name,
        desc = desc,
        delayMillis = delayMillis,
        block = block,
    )

    fun createInterval(
        name: String,
        desc: String? = null,
        period: Long,
        immediate: Boolean = true,
        block: suspend (task: Task) -> Unit
    ) = Task.createInterval(
        name = name,
        desc = desc,
        period = period,
        immediate = immediate,
        block = block,
    )

    fun success(
        id: String,
        result: Any? = null,
    ) = Task.success(
        id = id,
        result = result,
    )

    fun failure(
        id: String,
        result: Any? = null,
    ) = Task.failure(
        id = id,
        result = result,
    )

    fun cancel(
        id: String,
        result: Any? = null,
    ) = Task.cancel(
        id = id,
        result = result,
    )

    fun remove(id: String) = Task.remove(id)

    fun cancelAll() = Task.cancelAll()

    fun removeAll() = Task.removeAll()
}
