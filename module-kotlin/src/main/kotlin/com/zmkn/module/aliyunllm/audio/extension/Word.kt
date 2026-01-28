package com.zmkn.module.aliyunllm.audio.extension

import com.alibaba.dashscope.audio.tts.timestamp.Word
import com.zmkn.module.aliyunllm.audio.model.ResponseSpeechSynthesis

fun Word.toResponseSpeechSynthesisSentenceWord() = ResponseSpeechSynthesis.Sentence.Word(
    beginTime = beginTime,
    endTime = endTime,
    beginIndex = beginIndex,
    endIndex = endIndex,
    text = text,
    phonemes = phonemes?.map {
        it.toResponseSpeechSynthesisSentenceWordPhoneme()
    }
)
