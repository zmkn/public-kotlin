package com.zmkn.module.aliyunllm.audio.extension

import com.alibaba.dashscope.audio.ttsv2.enrollment.Voice
import com.zmkn.module.aliyunllm.audio.model.ResponseEnrolledVoice

fun Voice.toResponseEnrolledVoice(): ResponseEnrolledVoice = ResponseEnrolledVoice(
    voiceId = voiceId,
    status = if (status == null) {
        ResponseEnrolledVoice.Status.OK
    } else {
        ResponseEnrolledVoice.Status.fromValue(status)
    },
    gmtCreate = gmtCreate,
    gmtModified = gmtModified,
    targetModel = targetModel,
    resourceLink = resourceLink,
)
