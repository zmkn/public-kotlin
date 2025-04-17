package com.zmkn.module.aliyunllm.aigc.enumeration

enum class ResponseMessageChoiceFinishReason(val value: String) {
    STOP("stop"),
    LENGTH("length"),
    TOOL_CALLS("tool_calls");

    override fun toString(): String {
        return value
    }

    companion object {
        fun fromValue(value: String): ResponseMessageChoiceFinishReason {
            return when (value) {
                STOP.value -> STOP
                LENGTH.value -> LENGTH
                TOOL_CALLS.value -> TOOL_CALLS
                else -> throw IllegalArgumentException("ResponseMessageChoiceFinishReason value is not allowed.")
            }
        }
    }
}
