package com.zmkn.util

import java.util.*

object TimerUtils {
    /**
     * 设置一个定时任务，延迟指定的毫秒数后执行指定的操作，并返回一个 [Timer] 对象以供后续控制（如取消任务）。
     *
     * @param delay 延迟执行操作的时间，单位为毫秒。
     * @param finish 定时任务触发后执行的回调函数，接收一个 [Timer] 参数，通常用于在回调中执行实际操作或者取消定时器。
     * @return 返回创建的 [Timer] 对象，可以通过它来管理定时任务，例如调用 `cancel()` 方法取消定时任务。
     */
    fun setTimeout(
        delay: Long,
        finish: (timer: Timer) -> Unit,
    ): Timer {
        val timer = Timer()
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    finish(timer)
                }
            },
            delay
        )
        return timer
    }

    /**
     * 设置一个定时任务，指定日期到达后执行指定的操作，并返回一个 [Timer] 对象以供后续控制（如取消任务）。
     *
     * @param time 期望执行操作的日期时间点。
     * @param finish 定时任务触发后执行的回调函数，接收一个 [Timer] 参数，通常用于在回调中执行实际操作或者取消定时器。
     * @return 返回创建的 [Timer] 对象，可以通过它来管理定时任务，例如调用 `cancel()` 方法取消定时任务。
     */
    fun setTimeout(
        time: Date,
        finish: (timer: Timer) -> Unit,
    ): Timer {
        val timer = Timer()
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    finish(timer)
                }
            },
            time
        )
        return timer
    }

    fun setInterval(
        period: Long,
        immediate: Boolean,
        finish: (timer: Timer) -> Unit,
    ): Timer {
        val timer = Timer()
        val delay = if (immediate) 0L else period
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    finish(timer)
                }
            },
            delay,
            period
        )
        return timer
    }

    fun setInterval(
        period: Long,
        finish: (timer: Timer) -> Unit,
    ) = setInterval(
        period = period,
        immediate = true,
        finish = finish,
    )
}
