package com.zmkn.module.task.util

import com.zmkn.module.task.model.CreateParams
import kotlinx.coroutines.*

object TaskUtils {
    private val _jobs: MutableMap<String, Job> = mutableMapOf()
    private val _supervisorScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val jobs: Map<String, Job> = _jobs

    fun create(
        createParams: CreateParams,
        block: suspend (coroutineScope: CoroutineScope) -> Unit
    ): Job {
        return _supervisorScope.launch {
            createParams.delayMillis?.let {
                delay(it)
            }
            block(this)
        }.also {
            _jobs[createParams.name] = it
            it.invokeOnCompletion {
                _jobs.remove(createParams.name)
            }
        }
    }

    fun <T> create(
        createParams: CreateParams,
        block: suspend (coroutineScope: CoroutineScope) -> T
    ): Deferred<T> {
        return _supervisorScope.async {
            createParams.delayMillis?.let {
                delay(it)
            }
            block(this)
        }.also {
            _jobs[createParams.name] = it
            it.invokeOnCompletion {
                _jobs.remove(createParams.name)
            }
        }
    }

    fun cancel(taskName: String) {
        _jobs[taskName]?.cancel()
        _jobs.remove(taskName)
    }

    fun cancelAll() {
        _jobs.values.forEach {
            it.cancel()
        }
        _jobs.clear()
    }
}
