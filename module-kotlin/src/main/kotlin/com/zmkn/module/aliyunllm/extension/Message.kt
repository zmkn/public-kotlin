package com.zmkn.module.aliyunllm.extension

import com.alibaba.dashscope.common.Message
import com.zmkn.module.aliyunllm.enumeration.MessageRole
import com.zmkn.module.aliyunllm.model.ResponseMessage

fun Message.toResponseMessageOutputChoiceMessage(): ResponseMessage.Output.Choice.Message = ResponseMessage.Output.Choice.Message(
    role = MessageRole.fromValue(role),
    content = content,
    reasoningContent = reasoningContent,
    toolCalls = toolCalls?.mapNotNull {
        it.toResponseMessageOutputChoiceMessageToolCall()
    },
)
