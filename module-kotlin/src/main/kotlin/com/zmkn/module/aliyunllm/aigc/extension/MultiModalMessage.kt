package com.zmkn.module.aliyunllm.aigc.extension

import com.alibaba.dashscope.common.MultiModalMessage
import com.alibaba.dashscope.utils.JsonUtils
import com.zmkn.module.aliyunllm.aigc.enumeration.MessageRole
import com.zmkn.module.aliyunllm.aigc.model.MultiModalResponseMessage

fun MultiModalMessage.toMultiModalResponseMessageOutputChoiceMessage(): MultiModalResponseMessage.Output.Choice.Message {
    return MultiModalResponseMessage.Output.Choice.Message(
        role = MessageRole.fromValue(role),
        content = content.mapNotNull { map ->
            map.mapValues { (_, value) ->
                value as? String ?: JsonUtils.toJson(value)
            }
        },
        reasoningContent = reasoningContent,
    )
}
