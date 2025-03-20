package com.zmkn.module.aliyunllm.extension

import com.alibaba.dashscope.common.Message
import com.zmkn.module.aliyunllm.enumeration.MessageRole
import com.zmkn.module.aliyunllm.model.ResponseMessage

fun Message.toResponseMessageChoiceMessage(): ResponseMessage.Choice.Message = ResponseMessage.Choice.Message(
    role = MessageRole.fromValue(role),
    content = content,
    toolCalls =
    toolCalls?.mapNotNull {
        it.toResponseMessageChoiceMessageToolCall()
    },
)
