package com.zmkn.module.aliyunllm.extension

import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationUsage
import com.zmkn.module.aliyunllm.model.ResponseMessage

fun MultiModalConversationUsage.toResponseMessageUsage(): ResponseMessage.Usage = ResponseMessage.Usage(
    inputTokens = inputTokens,
    outputTokens = outputTokens,
    totalTokens = null,
)
