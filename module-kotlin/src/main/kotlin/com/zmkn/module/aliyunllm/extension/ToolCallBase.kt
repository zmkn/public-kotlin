package com.zmkn.module.aliyunllm.extension

import com.alibaba.dashscope.tools.ToolCallBase
import com.alibaba.dashscope.tools.ToolCallFunction
import com.zmkn.module.aliyunllm.model.ResponseMessage

fun ToolCallBase.toResponseMessageChoiceMessageToolCall(): ResponseMessage.Choice.Message.ToolCall? = if (this is ToolCallFunction) {
    ResponseMessage.Choice.Message.ToolCall(
        id = id,
        type = type,
        function = toResponseMessageChoiceMessageToolCallFunction(),
    )
} else {
    null
}
