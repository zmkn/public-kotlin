package com.zmkn.module.aliyunllm.extension

import com.alibaba.dashscope.tools.ToolCallFunction
import com.zmkn.module.aliyunllm.model.ResponseMessage

fun ToolCallFunction.toResponseMessageChoiceMessageToolCallFunction(): ResponseMessage.Choice.Message.ToolCall.Function = ResponseMessage.Choice.Message.ToolCall.Function(
    name = function.name,
    arguments = function.arguments,
)
