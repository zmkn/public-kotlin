package com.zmkn.module.alillm.extension

import com.alibaba.dashscope.common.MultiModalMessage
import com.zmkn.module.alillm.enumeration.MessageRole
import com.zmkn.module.alillm.model.ResponseMessage

fun MultiModalMessage.toResponseMessageChoiceMessage(): ResponseMessage.Choice.Message {
    val content =
        if (content.size > 0) {
            (content[0]["text"] as? String) ?: ""
        } else {
            ""
        }
    return ResponseMessage.Choice.Message(
        role = MessageRole.fromValue(role),
        content = content,
        toolCalls = null,
    )
}
