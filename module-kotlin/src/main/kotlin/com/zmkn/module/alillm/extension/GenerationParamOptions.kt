package com.zmkn.module.alillm.extension

import com.zmkn.module.alillm.model.GenerationParamOptions
import com.alibaba.dashscope.tools.ToolCallFunction

fun GenerationParamOptions.Message.ToolCall.toToolCallBase() = ToolCallFunction().also {
    it.id = id
    it.type = type
    it.function = function.toToolCallBaseFunction()
}

fun GenerationParamOptions.Message.ToolCall.Function.toToolCallBaseFunction() = ToolCallFunction().CallFunction().also {
    it.name = name
    it.arguments = arguments
}
