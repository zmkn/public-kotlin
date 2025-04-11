package com.zmkn.module.aliyunllm.extension

import com.alibaba.dashscope.common.MultiModalMessage
import com.alibaba.dashscope.utils.JsonUtils
import com.zmkn.module.aliyunllm.enumeration.MessageRole
import com.zmkn.module.aliyunllm.model.MultiModalResponseMessage

fun MultiModalMessage.toMultiModalResponseMessageOutputChoiceMessage(): MultiModalResponseMessage.Output.Choice.Message {
    return MultiModalResponseMessage.Output.Choice.Message(
        role = MessageRole.fromValue(role),
        content = content.mapNotNull { map ->
            map.mapValues { (_, value) ->
                JsonUtils.toJson(value)
            }
        },
        reasoningContent = reasoningContent,
    )
}
