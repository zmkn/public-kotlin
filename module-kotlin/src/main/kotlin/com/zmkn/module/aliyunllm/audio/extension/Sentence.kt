package com.zmkn.module.aliyunllm.audio.extension

import com.alibaba.dashscope.audio.tts.timestamp.Sentence
import com.zmkn.module.aliyunllm.audio.model.ResponseSpeechSynthesis

fun Sentence.toResponseSpeechSynthesisSentence() = ResponseSpeechSynthesis.Sentence(
    beginTime = beginTime,
    endTime = endTime,
    index = index,
    words = words?.map {
        it.toResponseSpeechSynthesisSentenceWord()
    }
)
