package com.zmkn.module.aliyunllm.extension

import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationResult
import com.zmkn.module.aliyunllm.model.MultiModalResponseMessage

fun MultiModalConversationResult.toMultiModalResponseMessage(): MultiModalResponseMessage {
    return MultiModalResponseMessage(
        requestId = requestId,
        usage = usage.toMultiModalResponseMessageUsage(),
        output = output.toMultiModalResponseMessageMessageOutput(),
    )
}
