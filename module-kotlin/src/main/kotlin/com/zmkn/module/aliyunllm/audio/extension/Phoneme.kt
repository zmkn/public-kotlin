package com.zmkn.module.aliyunllm.audio.extension

import com.alibaba.dashscope.audio.tts.timestamp.Phoneme
import com.zmkn.module.aliyunllm.audio.model.ResponseSpeechSynthesis

fun Phoneme.toResponseSpeechSynthesisSentenceWordPhoneme() = ResponseSpeechSynthesis.Sentence.Word.Phoneme(
    beginTime = beginTime,
    endTime = endTime,
    text = text,
    tone = tone
)
