package com.zmkn.module.aliyunllm.audio.model

data class ResponseSpeechSynthesis(
    val usage: Usage? = null,
    val audios: ByteArray? = null,
) {
    data class Usage(
        val characters: Int,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ResponseSpeechSynthesis

        if (usage != other.usage) return false
        if (!audios.contentEquals(other.audios)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = usage?.hashCode() ?: 0
        result = 31 * result + (audios?.contentHashCode() ?: 0)
        return result
    }
}
