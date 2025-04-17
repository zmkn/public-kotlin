package com.zmkn.module.aliyunllm.aigc.extension

import com.alibaba.dashscope.common.Message
import com.zmkn.module.aliyunllm.aigc.enumeration.MessageRole
import com.zmkn.module.aliyunllm.aigc.model.ResponseMessage

fun Message.toResponseMessageOutputChoiceMessage(): ResponseMessage.Output.Choice.Message = ResponseMessage.Output.Choice.Message(
    role = MessageRole.fromValue(role),
    content = content,
    reasoningContent = reasoningContent,
    toolCalls = toolCalls?.mapNotNull {
        it.toResponseMessageOutputChoiceMessageToolCall()
    },
)
