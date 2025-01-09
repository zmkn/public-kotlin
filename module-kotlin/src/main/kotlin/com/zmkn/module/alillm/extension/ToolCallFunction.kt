package com.zmkn.module.alillm.extension

import com.zmkn.module.alillm.model.ResponseMessage
import com.alibaba.dashscope.tools.ToolCallFunction

fun ToolCallFunction.toResponseMessageChoiceMessageToolCallFunction(): ResponseMessage.Choice.Message.ToolCall.Function = ResponseMessage.Choice.Message.ToolCall.Function(
    name = function.name,
    arguments = function.arguments,
)
