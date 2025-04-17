package com.zmkn.module.aliyunllm.aigc.extension

import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationOutput
import com.zmkn.module.aliyunllm.aigc.enumeration.ResponseMessageChoiceFinishReason
import com.zmkn.module.aliyunllm.aigc.model.MultiModalResponseMessage

fun MultiModalConversationOutput.toMultiModalResponseMessageMessageOutput(): MultiModalResponseMessage.Output = MultiModalResponseMessage.Output(
    choices = choices.mapNotNull {
        it.toMultiModalResponseMessageMessageOutputChoice()
    },
    audio = audio?.toMultiModalResponseMessageOutputAudio(),
)

fun MultiModalConversationOutput.Choice.toMultiModalResponseMessageMessageOutputChoice(): MultiModalResponseMessage.Output.Choice = MultiModalResponseMessage.Output.Choice(
    finishReason = if (!finishReason.isNullOrEmpty() && finishReason != "null") {
        ResponseMessageChoiceFinishReason.fromValue(finishReason)
    } else {
        null
    },
    message = message.toMultiModalResponseMessageOutputChoiceMessage(),
)
