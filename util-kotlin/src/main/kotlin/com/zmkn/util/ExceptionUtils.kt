package com.zmkn.util

object ExceptionUtils {
    fun interface RunBody<T> {
        fun run(): T
    }

    fun interface RunBodyCoroutine<T> {
        suspend fun run(): T
    }

    fun <T> register(runBody: RunBody<T>): Result<T> {
        return try {
            Result.success(runBody.run())
        } catch (error: Throwable) {
            Result.failure(error)
        }
    }

    suspend fun <T> registerCoroutine(runBodyCoroutine: RunBodyCoroutine<T>): Result<T> {
        return try {
            Result.success(runBodyCoroutine.run())
        } catch (error: Throwable) {
            Result.failure(error)
        }
    }

    fun rethrow(error: Any) {
        if (error is Throwable) {
            throw error
        }
    }
}
