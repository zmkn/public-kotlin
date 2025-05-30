package com.zmkn.module.aliyunllm.aigc.extension

import com.zmkn.module.aliyunllm.aigc.model.GenerationParamOptions
import com.zmkn.module.aliyunllm.aigc.model.GenerationParamOptions.Message.ToolCall
import com.zmkn.module.aliyunllm.aigc.model.ResponseMessage

fun ResponseMessage.Output.Choice.Message.toGenerationParamOptionsMessage(): GenerationParamOptions.Message {
    var toolCallId: String? = null
    var toolCalls: List<ToolCall>? = null
    if (!this.toolCalls.isNullOrEmpty()) {
        toolCallId = this.toolCalls[0].id
        toolCalls = this.toolCalls.map {
            it.toToolCallBase()
        }
    }
    return GenerationParamOptions.Message(
        role = role,
        content = content,
        toolCallId = toolCallId,
        toolCalls = toolCalls,
    )
}

fun ResponseMessage.Output.Choice.Message.ToolCall.toToolCallBase(): ToolCall = ToolCall(
    id = id,
    type = type,
    function = function.toGenerationParamOptionsMessageToolCallFunction()
)

fun ResponseMessage.Output.Choice.Message.ToolCall.Function.toGenerationParamOptionsMessageToolCallFunction(): ToolCall.Function = ToolCall.Function(
    name = name,
    arguments = arguments,
    output = output,
)
