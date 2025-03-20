package com.zmkn.module.aliyunllm.extension

import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationResult
import com.zmkn.module.aliyunllm.enumeration.ResponseMessageChoiceFinishReason
import com.zmkn.module.aliyunllm.model.ResponseMessage

fun MultiModalConversationResult.toResponseMessage(): ResponseMessage {
    val choices =
        output.choices.map { choice ->
            ResponseMessage.Choice(
                finishReason =
                if (!choice.finishReason.isNullOrEmpty() && choice.finishReason != "null") {
                    ResponseMessageChoiceFinishReason.fromValue(choice.finishReason)
                } else {
                    null
                },
                message = choice.message.toResponseMessageChoiceMessage(),
            )
        }
    return ResponseMessage(
        requestId = requestId,
        usage = usage.toResponseMessageUsage(),
        choices = choices,
    )
}
