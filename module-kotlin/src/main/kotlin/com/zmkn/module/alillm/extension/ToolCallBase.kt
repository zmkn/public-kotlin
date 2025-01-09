package com.zmkn.module.alillm.extension

import com.zmkn.module.alillm.model.ResponseMessage
import com.alibaba.dashscope.tools.ToolCallBase
import com.alibaba.dashscope.tools.ToolCallFunction

fun ToolCallBase.toResponseMessageChoiceMessageToolCall(): ResponseMessage.Choice.Message.ToolCall? = if (this is ToolCallFunction) {
    ResponseMessage.Choice.Message.ToolCall(
        id = id,
        type = type,
        function = toResponseMessageChoiceMessageToolCallFunction(),
    )
} else {
    null
}
