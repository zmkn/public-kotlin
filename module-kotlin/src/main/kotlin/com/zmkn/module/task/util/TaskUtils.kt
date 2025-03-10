package com.zmkn.module.task.util

import com.zmkn.module.task.enumeration.TaskStatus
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
        name: String,
        desc: String?,
        delayMillis: Long?,
        block: suspend (coroutineScope: CoroutineScope) -> Any?
    ): String {
        val id = generateTaskId()
        return _supervisorScope.launch {
            try {
                delayMillis?.let {
                    delay(it)
                }
                val result = block(this)
                _jobs[id]?.also {
                    _jobs[id] = it.copy(
                        status = TaskStatus.SUCCEEDED,
                        result = result,
                    )
                }
            } catch (e: Exception) {
                _jobs[id]?.also {
                    if (!it.job.isCancelled) {
                        _jobs[id] = it.copy(
                            status = TaskStatus.FAILED,
                            result = e,
                        )
                    }
                }
            }
        }.let {
            _jobs[id] = Task(
                id = id,
                job = it,
                name = name,
                status = TaskStatus.NORMAL,
                desc = desc,
                result = null,
            )
            id
        }
    }

    fun create(
        name: String,
        delayMillis: Long,
        block: suspend (coroutineScope: CoroutineScope) -> Any?
    ): String = create(
        name = name,
        desc = null,
        delayMillis = delayMillis,
        block = block,
    )

    fun create(
        name: String,
        desc: String,
        block: suspend (coroutineScope: CoroutineScope) -> Any?
    ): String = create(
        name = name,
        desc = desc,
        delayMillis = null,
        block = block,
    )

    fun create(
        name: String,
        block: suspend (coroutineScope: CoroutineScope) -> Any?
    ): String = create(
        name = name,
        desc = null,
        delayMillis = null,
        block = block,
    )

    fun cancel(id: String) {
        _jobs[id]?.also {
            it.job.cancel()
            _jobs[id] = it.copy(
                status = TaskStatus.CANCELED
            )
        }
    }

    fun cancelAll() {
        _jobs.forEach { id, task ->
            task.job.cancel()
            _jobs[id] = task.copy(
                status = TaskStatus.CANCELED
            )
        }
    }

    fun remove(id: String) {
        cancel(id)
        _jobs.remove(id)
    }

    fun removeAll() {
        cancelAll()
        _jobs.clear()
    }
}
