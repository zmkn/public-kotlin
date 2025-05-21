package com.zmkn.module.task

import com.zmkn.module.task.enumeration.TaskStatus
import com.zmkn.util.RandomUtils
import kotlinx.coroutines.*

data class Task(
    val id: String,
    val job: Job,
    val name: String,
    val status: TaskStatus,
    val desc: String? = null,
    val result: Any? = null,
) {
    fun success(result: Any? = null) = success(id, result)

    fun failure(result: Any? = null) = failure(id, result)

    fun cancel(result: Any? = null) = cancel(id, result)

    fun remove() = remove(id)

    companion object {
        private val _jobs: MutableMap<String, Task> = mutableMapOf()
        private val _supervisorScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        private fun generateTaskId(): String {
            return "task-${RandomUtils.generateRandomNumber(12)}"
        }

        val jobs: Map<String, Task> = _jobs

        fun getTask(id: String): Task? {
            return _jobs[id]
        }

        fun create(
            name: String,
            desc: String? = null,
            delayMillis: Long? = null,
            block: suspend (task: Task) -> Any?
        ): String {
            val id = generateTaskId()
            return _supervisorScope.launch {
                try {
                    val task = getTask(id)!!
                    delayMillis?.let {
                        delay(it)
                    }
                    val result = block(task)
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

        fun createInterval(
            name: String,
            desc: String? = null,
            period: Long,
            immediate: Boolean = true,
            block: suspend (task: Task) -> Unit
        ): String {
            val id = generateTaskId()
            return _supervisorScope.launch {
                try {
                    val task = getTask(id)!!
                    if (immediate) {
                        block(task)
                    }
                    while (isActive) {
                        delay(period)
                        block(task)
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

        fun success(
            id: String,
            result: Any? = null,
        ) {
            _jobs[id]?.also {
                it.job.cancel()
                _jobs[id] = it.copy(
                    status = TaskStatus.SUCCEEDED,
                    result = result,
                )
            }
        }

        fun failure(
            id: String,
            result: Any? = null,
        ) {
            _jobs[id]?.also {
                it.job.cancel()
                _jobs[id] = it.copy(
                    status = TaskStatus.FAILED,
                    result = result,
                )
            }
        }

        fun cancel(
            id: String,
            result: Any? = null,
        ) {
            _jobs[id]?.also {
                it.job.cancel()
                _jobs[id] = it.copy(
                    status = TaskStatus.CANCELED,
                    result = result,
                )
            }
        }

        fun remove(id: String) {
            cancel(id)
            _jobs.remove(id)
        }

        fun cancelAll() {
            _jobs.forEach { id, task ->
                task.job.cancel()
                _jobs[id] = task.copy(
                    status = TaskStatus.CANCELED
                )
            }
        }

        fun removeAll() {
            cancelAll()
            _jobs.clear()
        }
    }
}
