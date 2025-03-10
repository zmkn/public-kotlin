package com.zmkn.module.task.enumeration

enum class TaskStatus(val value: String) {
    NORMAL("NORMAL"),
    CANCELED("CANCELED"),
    SUCCEEDED("SUCCEEDED"),
    FAILED("FAILED");

    override fun toString(): String {
        return value
    }
}
