package com.zmkn.module.aliyunllm.aigc.model

data class ResponseFormat(
    val type: Type,
) {
    enum class Type(val value: String) {
        TEXT("text"),
        JSON_OBJECT("json_object");

        override fun toString(): String = value

        companion object {
            fun fromValue(value: String): Type = when (value) {
                TEXT.value -> TEXT
                JSON_OBJECT.value -> JSON_OBJECT
                else -> throw IllegalArgumentException("Type value is not allowed.")
            }
        }
    }
}
