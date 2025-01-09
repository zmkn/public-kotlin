package com.zmkn.module.alillm.extension

import com.alibaba.dashscope.aigc.generation.GenerationResult
import com.zmkn.module.alillm.enumeration.ResponseMessageChoiceFinishReason
import com.zmkn.module.alillm.model.ResponseMessage

fun GenerationResult.toResponseMessage(): ResponseMessage {
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
