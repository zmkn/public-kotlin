package com.zmkn.module.aliyunllm.aigc.extension

import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationUsage
import com.zmkn.module.aliyunllm.aigc.model.MultiModalResponseMessage

fun MultiModalConversationUsage.toMultiModalResponseMessageUsage(): MultiModalResponseMessage.Usage = MultiModalResponseMessage.Usage(
    inputTokens = inputTokens,
    outputTokens = outputTokens,
    totalTokens = totalTokens,
    imageTokens = imageTokens,
    videoTokens = videoTokens,
    audioTokens = audioTokens,
    inputTokensDetails = inputTokensDetails.toMultiModalResponseMessageUsageTokensDetails(),
    outputTokensDetails = outputTokensDetails.toMultiModalResponseMessageUsageTokensDetails(),
)
