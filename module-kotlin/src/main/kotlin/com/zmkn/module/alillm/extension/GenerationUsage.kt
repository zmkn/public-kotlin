package com.zmkn.module.alillm.extension

import com.alibaba.dashscope.aigc.generation.GenerationUsage
import com.zmkn.module.alillm.model.ResponseMessage

fun GenerationUsage.toResponseMessageUsage(): ResponseMessage.Usage = ResponseMessage.Usage(
    inputTokens = inputTokens,
    outputTokens = outputTokens,
    totalTokens = totalTokens,
)
