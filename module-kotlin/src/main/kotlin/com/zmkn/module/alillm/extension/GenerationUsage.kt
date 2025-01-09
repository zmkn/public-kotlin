package com.zmkn.module.alillm.extension

import com.zmkn.module.alillm.model.ResponseMessage
import com.alibaba.dashscope.aigc.generation.GenerationUsage

fun GenerationUsage.toResponseMessageUsage(): ResponseMessage.Usage = ResponseMessage.Usage(
    inputTokens = inputTokens,
    outputTokens = outputTokens,
    totalTokens = totalTokens,
)
