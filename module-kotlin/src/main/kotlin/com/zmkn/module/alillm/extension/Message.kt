package com.zmkn.module.alillm.extension

import com.alibaba.dashscope.common.Message
import com.zmkn.module.alillm.enumeration.MessageRole
import com.zmkn.module.alillm.model.ResponseMessage

fun Message.toResponseMessageChoiceMessage(): ResponseMessage.Choice.Message = ResponseMessage.Choice.Message(
    role = MessageRole.fromValue(role),
    content = content,
    toolCalls =
    toolCalls?.mapNotNull {
        it.toResponseMessageChoiceMessageToolCall()
    },
)
