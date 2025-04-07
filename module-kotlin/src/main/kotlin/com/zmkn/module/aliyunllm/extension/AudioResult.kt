package com.zmkn.module.aliyunllm.extension

import com.alibaba.dashscope.aigc.multimodalconversation.AudioResult
import com.zmkn.module.aliyunllm.model.MultiModalResponseMessage

fun AudioResult.toMultiModalResponseMessageOutputAudio(): MultiModalResponseMessage.Output.Audio = MultiModalResponseMessage.Output.Audio(
    id = id,
    url = url,
    data = data,
    expiresAt = expiresAt,
)
