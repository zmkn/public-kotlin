package com.zmkn.module.aliyunllm.audio.extension

import com.alibaba.dashscope.audio.tts.SpeechSynthesisResult
import com.zmkn.module.aliyunllm.audio.model.ResponseSpeechSynthesis

fun SpeechSynthesisResult.toResponseSpeechSynthesis(): ResponseSpeechSynthesis = ResponseSpeechSynthesis(
    requestId = requestId,
    usage = usage?.toResponseSpeechSynthesisUsage(),
    audios = audioFrame?.array(),
    timestamp = timestamp
)
