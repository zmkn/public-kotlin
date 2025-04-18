package com.zmkn.module.aliyunllm.audio.extension

import com.alibaba.dashscope.audio.ttsv2.enrollment.Voice
import com.zmkn.module.aliyunllm.audio.model.ResponseVoice

fun Voice.toResponseVoice(): ResponseVoice = ResponseVoice(
    voiceId = voiceId,
    status = if (status == null) {
        ResponseVoice.Status.OK
    } else {
        ResponseVoice.Status.fromValue(status)
    },
    gmtCreate = gmtCreate,
    gmtModified = gmtModified,
)
