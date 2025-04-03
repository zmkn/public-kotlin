package com.zmkn.enumeration

enum class SseEvent(val value: String) {
    MESSAGE("message"),
    ERROR("error");

    override fun toString(): String {
        return value
    }

    companion object {
        fun fromValue(value: String): SseEvent? {
            SseEvent.entries.forEach {
                if (value == it.value) {
                    return it
                }
            }
            return null
        }
    }
}
