package com.zmkn.module.aliyunllm.extension

import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationTokensDetails
import com.zmkn.module.aliyunllm.model.MultiModalResponseMessage

fun MultiModalConversationTokensDetails.toMultiModalResponseMessageUsageTokensDetails(): MultiModalResponseMessage.Usage.TokensDetails = MultiModalResponseMessage.Usage.TokensDetails(
    textTokens = textTokens,
    imageTokens = imageTokens,
    audioTokens = audioTokens,
    videoTokens = videoTokens,
)
