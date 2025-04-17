package com.zmkn.module.aliyunllm.audio.util

object AudioUtils {
    const val SPLIT_POSITION_LENGTH = 2000

    val punctuationRegex = "\\p{P}".toRegex()

    fun splitSpeechSynthesizerText(text: String): List<String> {
        val newText = text.trim()
        val texts = mutableListOf<String>()
        if (newText.isNotBlank()) {
            val matches = punctuationRegex.findAll(newText).toList()
            for (i in matches.size - 1 downTo 0) {
                val match = matches[i]
                val endIndex = match.range.last + 1
                val part = newText.substring(0, endIndex)
                if (CharacterUtils.calculateStringLength(part) <= SPLIT_POSITION_LENGTH) {
                    texts.add(part)
                    if (endIndex != newText.length) {
                        texts.addAll(splitSpeechSynthesizerText(newText.substring(endIndex)))
                    }
                    break
                } else if (i == 0) {
                    throw IllegalArgumentException("Text length cannot exceed $SPLIT_POSITION_LENGTH characters.")
                }
            }
        }
        return texts
    }

    fun formatSpeechSynthesizerTexts(texts: List<String>): List<String> {
        return texts.map { text ->
            splitSpeechSynthesizerText(text)
        }.flatten()
    }
}
