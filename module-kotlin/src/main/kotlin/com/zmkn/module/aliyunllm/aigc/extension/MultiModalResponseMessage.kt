package com.zmkn.module.aliyunllm.aigc.extension

import com.zmkn.module.aliyunllm.aigc.model.MultiModalConversationParamOptions
import com.zmkn.module.aliyunllm.aigc.model.MultiModalMessageContent
import com.zmkn.module.aliyunllm.aigc.model.MultiModalResponseMessage

fun MultiModalResponseMessage.Output.Choice.Message.toMultiModalConversationParamOptionsMessage(): MultiModalConversationParamOptions.Message {
    val contents = mutableListOf<MultiModalMessageContent>()
    content.forEach { map ->
        map.forEach {
            if (it.key == "text" && it.value.isNotEmpty()) {
                contents.add(MultiModalMessageContent.Text(it.value))
            }
        }
    }
    return MultiModalConversationParamOptions.Message(
        role = role,
        contents = contents
    )
}
