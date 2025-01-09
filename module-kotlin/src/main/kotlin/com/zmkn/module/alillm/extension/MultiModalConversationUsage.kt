package com.zmkn.module.alillm.extension

import com.zmkn.module.alillm.model.ResponseMessage
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationUsage

fun MultiModalConversationUsage.toResponseMessageUsage(): ResponseMessage.Usage = ResponseMessage.Usage(
    inputTokens = inputTokens,
    outputTokens = outputTokens,
    totalTokens = null,
)
