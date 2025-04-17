package com.zmkn.module.aliyunllm.audio.extension

import com.alibaba.dashscope.audio.tts.SpeechSynthesisResult
import com.zmkn.module.aliyunllm.audio.model.ResponseSpeechSynthesis

fun SpeechSynthesisResult.toResponseSpeechSynthesis(): ResponseSpeechSynthesis = ResponseSpeechSynthesis(
    usage = usage?.toResponseSpeechSynthesisUsage(),
    audios = audioFrame?.array(),
)
