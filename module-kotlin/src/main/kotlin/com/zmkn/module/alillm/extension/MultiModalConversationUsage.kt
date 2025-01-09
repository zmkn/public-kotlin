package com.zmkn.module.alillm.extension

import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationUsage
import com.zmkn.module.alillm.model.ResponseMessage

fun MultiModalConversationUsage.toResponseMessageUsage(): ResponseMessage.Usage = ResponseMessage.Usage(
    inputTokens = inputTokens,
    outputTokens = outputTokens,
    totalTokens = null,
)
