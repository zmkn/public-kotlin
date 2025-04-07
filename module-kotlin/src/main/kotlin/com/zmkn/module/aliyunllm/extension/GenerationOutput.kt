package com.zmkn.module.aliyunllm.extension

import com.alibaba.dashscope.aigc.generation.GenerationOutput
import com.zmkn.module.aliyunllm.enumeration.ResponseMessageChoiceFinishReason
import com.zmkn.module.aliyunllm.model.ResponseMessage

fun GenerationOutput.toResponseMessageOutput(): ResponseMessage.Output = ResponseMessage.Output(
    choices = choices.mapNotNull {
        it.toResponseMessageOutputChoice()
    },
    searchInfo = searchInfo?.toResponseMessageOutputSearchInfo()
)

fun GenerationOutput.Choice.toResponseMessageOutputChoice(): ResponseMessage.Output.Choice = ResponseMessage.Output.Choice(
    finishReason = if (!finishReason.isNullOrEmpty() && finishReason != "null") {
        ResponseMessageChoiceFinishReason.fromValue(finishReason)
    } else {
        null
    },
    message = message.toResponseMessageOutputChoiceMessage(),
)
