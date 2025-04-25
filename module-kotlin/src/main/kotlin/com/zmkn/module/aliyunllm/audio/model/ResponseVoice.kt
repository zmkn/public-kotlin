package com.zmkn.module.aliyunllm.audio.model

data class ResponseVoice(
    val voiceId: String? = null,
    val status: Status,
    val gmtCreate: String? = null,
    val gmtModified: String? = null,
    val targetModel: String? = null,
    val resourceLink: String? = null,
) {
    enum class Status(val value: String) {
        OK("OK"),
        DEPLOYING("DEPLOYING"),
        UNDEPLOYED("UNDEPLOYED");

        override fun toString(): String {
            return value
        }

        companion object {
            fun fromValue(value: String): Status {
                return when (value) {
                    OK.value -> OK
                    DEPLOYING.value -> DEPLOYING
                    UNDEPLOYED.value -> UNDEPLOYED
                    else -> throw IllegalArgumentException("Status value is not allowed.")
                }
            }
        }
    }
}
