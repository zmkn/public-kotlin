package com.zmkn.module.aliyunllm.aigc.extension

import com.alibaba.dashscope.tools.ToolCallBase
import com.alibaba.dashscope.tools.ToolCallFunction
import com.zmkn.module.aliyunllm.aigc.model.ResponseMessage

fun ToolCallBase.toResponseMessageOutputChoiceMessageToolCall(): ResponseMessage.Output.Choice.Message.ToolCall? = if (this is ToolCallFunction) {
    ResponseMessage.Output.Choice.Message.ToolCall(
        id = id,
        type = type,
        function = toResponseMessageOutputChoiceMessageToolCallFunction(),
    )
} else {
    null
}
