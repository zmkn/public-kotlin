package com.zmkn.module.aliyunllm.aigc.model

data class ToolChoice(
    val type: Type = Type.AUTO,
    val function: Function? = null,
) {
    enum class Type(val value: String) {
        AUTO("auto"),
        NONE("none"),
        FUNCTION("function"), ;

        override fun toString(): String = value

        companion object {
            fun fromValue(value: String): Type = when (value) {
                AUTO.value -> AUTO
                NONE.value -> NONE
                FUNCTION.value -> FUNCTION
                else -> throw IllegalArgumentException("Type value is not allowed.")
            }
        }
    }

    data class Function(
        val name: String,
    )
}
