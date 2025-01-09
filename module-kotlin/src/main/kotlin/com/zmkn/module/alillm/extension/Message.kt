package com.zmkn.module.alillm.extension

import com.zmkn.module.alillm.enumeration.MessageRole
import com.zmkn.module.alillm.model.ResponseMessage
import com.alibaba.dashscope.common.Message

fun Message.toResponseMessageChoiceMessage(): ResponseMessage.Choice.Message = ResponseMessage.Choice.Message(
    role = com.zmkn.module.alillm.enumeration.MessageRole.fromValue(role),
    content = content,
    toolCalls =
    toolCalls?.mapNotNull {
        it.toResponseMessageChoiceMessageToolCall()
    },
)
