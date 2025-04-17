package com.zmkn.module.aliyunllm.aigc.extension

import com.alibaba.dashscope.aigc.generation.GenerationUsage
import com.zmkn.module.aliyunllm.aigc.model.ResponseMessage

fun GenerationUsage.toResponseMessageUsage(): ResponseMessage.Usage = ResponseMessage.Usage(
    inputTokens = inputTokens,
    outputTokens = outputTokens,
    totalTokens = totalTokens,
)
