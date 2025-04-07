package com.zmkn.module.aliyunllm.extension

import com.alibaba.dashscope.tools.ToolCallBase
import com.alibaba.dashscope.tools.ToolCallFunction
import com.zmkn.module.aliyunllm.model.ResponseMessage

fun ToolCallBase.toResponseMessageOutputChoiceMessageToolCall(): ResponseMessage.Output.Choice.Message.ToolCall? = if (this is ToolCallFunction) {
    ResponseMessage.Output.Choice.Message.ToolCall(
        id = id,
        type = type,
        function = toResponseMessageOutputChoiceMessageToolCallFunction(),
    )
} else {
    null
}
