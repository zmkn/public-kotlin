package com.zmkn.module.alillm.extension

import com.zmkn.module.alillm.enumeration.MessageRole
import com.zmkn.module.alillm.model.ResponseMessage
import com.alibaba.dashscope.common.MultiModalMessage

fun MultiModalMessage.toResponseMessageChoiceMessage(): ResponseMessage.Choice.Message {
    val content =
        if (content.size > 0) {
            (content[0]["text"] as? String) ?: ""
        } else {
            ""
        }
    return ResponseMessage.Choice.Message(
        role = com.zmkn.module.alillm.enumeration.MessageRole.fromValue(role),
        content = content,
        toolCalls = null,
    )
}
