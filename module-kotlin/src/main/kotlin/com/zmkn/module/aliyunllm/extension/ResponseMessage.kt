package com.zmkn.module.aliyunllm.extension

import com.zmkn.module.aliyunllm.model.GenerationParamOptions
import com.zmkn.module.aliyunllm.model.GenerationParamOptions.Message.ToolCall
import com.zmkn.module.aliyunllm.model.MultiModalConversationParamOptions
import com.zmkn.module.aliyunllm.model.MultiModalMessageContent
import com.zmkn.module.aliyunllm.model.ResponseMessage

fun ResponseMessage.Choice.Message.toGenerationParamOptionsMessage(): GenerationParamOptions.Message {
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

fun ResponseMessage.Choice.Message.ToolCall.toToolCallBase(): ToolCall = ToolCall(
    id = id,
    type = type,
    function = function.toGenerationParamOptionsMessageToolCallFunction()
)

fun ResponseMessage.Choice.Message.ToolCall.Function.toGenerationParamOptionsMessageToolCallFunction(): ToolCall.Function = ToolCall.Function(
    name = name,
    arguments = arguments
)

fun ResponseMessage.Choice.Message.toMultiModalConversationParamOptionsMessage(): MultiModalConversationParamOptions.Message {
    val contents = mutableListOf<MultiModalMessageContent>()
    if (content.isNotEmpty()) {
        contents.add(MultiModalMessageContent.Text(content))
    }
    return MultiModalConversationParamOptions.Message(
        role = role,
        contents = contents
    )
}
