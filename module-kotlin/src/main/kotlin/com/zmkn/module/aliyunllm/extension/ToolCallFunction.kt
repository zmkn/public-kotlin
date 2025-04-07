package com.zmkn.module.aliyunllm.extension

import com.alibaba.dashscope.tools.ToolCallFunction
import com.zmkn.module.aliyunllm.model.ResponseMessage

fun ToolCallFunction.toResponseMessageOutputChoiceMessageToolCallFunction(): ResponseMessage.Output.Choice.Message.ToolCall.Function = ResponseMessage.Output.Choice.Message.ToolCall.Function(
    name = function.name,
    arguments = function.arguments,
    output = function.output,
)
