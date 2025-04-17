package com.zmkn.module.aliyunllm.aigc.extension

import com.alibaba.dashscope.aigc.generation.GenerationResult
import com.zmkn.module.aliyunllm.aigc.model.ResponseMessage

fun GenerationResult.toResponseMessage(): ResponseMessage {
    return ResponseMessage(
        requestId = requestId,
        usage = usage.toResponseMessageUsage(),
        output = output.toResponseMessageOutput(),
    )
}
