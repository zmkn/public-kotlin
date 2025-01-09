package com.zmkn.module.alillm.enumeration

enum class MessageRole(val value: String) {
    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant"),
    TOOL("tool");

    override fun toString(): String {
        return value
    }

    companion object {
        fun fromValue(value: String): com.zmkn.module.alillm.enumeration.MessageRole {
            return when (value) {
                com.zmkn.module.alillm.enumeration.MessageRole.SYSTEM.value -> com.zmkn.module.alillm.enumeration.MessageRole.SYSTEM
                com.zmkn.module.alillm.enumeration.MessageRole.USER.value -> com.zmkn.module.alillm.enumeration.MessageRole.USER
                com.zmkn.module.alillm.enumeration.MessageRole.ASSISTANT.value -> com.zmkn.module.alillm.enumeration.MessageRole.ASSISTANT
                com.zmkn.module.alillm.enumeration.MessageRole.TOOL.value -> com.zmkn.module.alillm.enumeration.MessageRole.TOOL
                else -> throw IllegalArgumentException("MessageRole code is not allowed.")
            }
        }
    }
}
