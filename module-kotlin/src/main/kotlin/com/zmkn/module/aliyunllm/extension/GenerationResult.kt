package com.zmkn.module.aliyunllm.extension

import com.alibaba.dashscope.aigc.generation.GenerationResult
import com.zmkn.module.aliyunllm.model.ResponseMessage

fun GenerationResult.toResponseMessage(): ResponseMessage {
    return ResponseMessage(
        requestId = requestId,
        usage = usage.toResponseMessageUsage(),
        output = output.toResponseMessageOutput(),
    )
}
