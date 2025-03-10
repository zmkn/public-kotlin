package com.zmkn.module.task.util

import com.zmkn.module.task.model.CreateParams
import com.zmkn.module.task.model.Task
import com.zmkn.util.RandomUtils
import kotlinx.coroutines.*

object TaskUtils {
    private val _jobs: MutableMap<String, Task> = mutableMapOf()
    private val _supervisorScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val jobs: Map<String, Task> = _jobs

    private fun generateTaskId(): String {
        return "task-${RandomUtils.generateRandomNumber(12)}"
    }

    fun getJob(id: String): Job? {
        return _jobs[id]?.job
    }

    fun create(
        createParams: CreateParams,
        block: suspend (coroutineScope: CoroutineScope) -> Unit
    ): String {
        return _supervisorScope.launch {
            createParams.delayMillis?.let {
                delay(it)
            }
            block(this)
        }.let {
            val id = generateTaskId()
            _jobs[id] = Task(
                id = id,
                name = createParams.name,
                desc = createParams.desc,
                job = it,
            )
            it.invokeOnCompletion {
                _jobs.remove(id)
            }
            id
        }
    }

    fun create(
        name: String,
        desc: String,
        delayMillis: Long,
        block: suspend (coroutineScope: CoroutineScope) -> Unit
    ): String = create(
        CreateParams(
            name = name,
            desc = desc,
            delayMillis = delayMillis,
        ), block
    )

    fun create(
        name: String,
        delayMillis: Long,
        block: suspend (coroutineScope: CoroutineScope) -> Unit
    ): String = create(
        CreateParams(
            name = name,
            delayMillis = delayMillis,
        ), block
    )

    fun create(
        name: String,
        desc: String,
        block: suspend (coroutineScope: CoroutineScope) -> Unit
    ): String = create(
        CreateParams(
            name = name,
            desc = desc,
        ), block
    )

    fun create(
        name: String,
        block: suspend (coroutineScope: CoroutineScope) -> Unit
    ): String = create(
        CreateParams(
            name = name,
        ), block
    )

    fun <T> create(
        createParams: CreateParams,
        block: suspend (coroutineScope: CoroutineScope) -> T
    ): String {
        return _supervisorScope.async {
            createParams.delayMillis?.let {
                delay(it)
            }
            block(this)
        }.let {
            val id = generateTaskId()
            _jobs[id] = Task(
                id = id,
                name = createParams.name,
                desc = createParams.desc,
                job = it,
            )
            it.invokeOnCompletion {
                _jobs.remove(id)
            }
            id
        }
    }

    fun <T> create(
        name: String,
        desc: String,
        delayMillis: Long,
        block: suspend (coroutineScope: CoroutineScope) -> T
    ): String = create(
        CreateParams(
            name = name,
            desc = desc,
            delayMillis = delayMillis,
        ), block
    )

    fun <T> create(
        name: String,
        delayMillis: Long,
        block: suspend (coroutineScope: CoroutineScope) -> T
    ): String = create(
        CreateParams(
            name = name,
            delayMillis = delayMillis,
        ), block
    )

    fun <T> create(
        name: String,
        desc: String,
        block: suspend (coroutineScope: CoroutineScope) -> T
    ): String = create(
        CreateParams(
            name = name,
            desc = desc,
        ), block
    )

    fun <T> create(
        name: String,
        block: suspend (coroutineScope: CoroutineScope) -> T
    ): String = create(
        CreateParams(
            name = name,
        ), block
    )

    fun cancel(id: String) {
        _jobs[id]?.job?.cancel()
        _jobs.remove(id)
    }

    fun cancelAll() {
        _jobs.values.forEach {
            it.job.cancel()
        }
        _jobs.clear()
    }
}
