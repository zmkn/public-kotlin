package com.zmkn.module.aliyunllm.audio.extension

import com.alibaba.dashscope.audio.tts.SpeechSynthesisUsage
import com.zmkn.module.aliyunllm.audio.model.ResponseSpeechSynthesis

fun SpeechSynthesisUsage.toResponseSpeechSynthesisUsage(): ResponseSpeechSynthesis.Usage = ResponseSpeechSynthesis.Usage(
    characters = characters
)
