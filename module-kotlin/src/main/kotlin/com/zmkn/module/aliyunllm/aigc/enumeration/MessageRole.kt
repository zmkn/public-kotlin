package com.zmkn.module.aliyunllm.aigc.enumeration

enum class MessageRole(val value: String) {
    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant"),
    TOOL("tool");

    override fun toString(): String = value

    companion object {
        fun fromValue(value: String): MessageRole = when (value) {
            SYSTEM.value -> SYSTEM
            USER.value -> USER
            ASSISTANT.value -> ASSISTANT
            TOOL.value -> TOOL
            else -> throw IllegalArgumentException("MessageRole value is not allowed.")
        }
    }
}
