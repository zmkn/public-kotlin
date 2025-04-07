package com.zmkn.module.aliyunllm.extension

import com.alibaba.dashscope.common.MultiModalMessage
import com.zmkn.module.aliyunllm.enumeration.MessageRole
import com.zmkn.module.aliyunllm.model.MultiModalResponseMessage

fun MultiModalMessage.toMultiModalResponseMessageOutputChoiceMessage(): MultiModalResponseMessage.Output.Choice.Message {
    return MultiModalResponseMessage.Output.Choice.Message(
        role = MessageRole.fromValue(role),
        content = content.mapNotNull { map ->
            map.mapValues { (_, value) ->
                value.toString()
            }
        },
        reasoningContent = reasoningContent,
    )
}
