package com.zmkn.module.aliyunllm.audio.model

data class ResponseSpeechSynthesis(
    val requestId: String? = null,
    val usage: Usage? = null,
    val audios: ByteArray? = null,
    val timestamp: Sentence? = null,
) {
    data class Usage(
        val characters: Int,
    )

    data class Sentence(
        val beginTime: Int? = null,
        val endTime: Int? = null,
        val index: Int? = null,
        val words: List<Word>? = null,
    ) {
        data class Word(
            val beginTime: Int? = null,
            val endTime: Int? = null,
            val beginIndex: Int? = null,
            val endIndex: Int? = null,
            val text: String? = null,
            val phonemes: List<Phoneme>? = null,
        ) {
            data class Phoneme(
                val beginTime: Int? = null,
                val endTime: Int? = null,
                val text: String? = null,
                val tone: String? = null,
            )
        }
    }

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
