package com.zmkn.module.aliyunllm

import com.zmkn.module.aliyunllm.aigc.Aigc
import com.zmkn.module.aliyunllm.aigc.model.GenerationParamOptions
import com.zmkn.module.aliyunllm.aigc.model.MultiModalConversationParamOptions

class AliyunLlm(
    apiKeys: List<String>
) : Base(apiKeys) {
    private val _aigc = Aigc(apiKeys)

    fun createStreamMessage(options: GenerationParamOptions) = _aigc.createStreamMessage(options)

    fun createStreamMultiModalMessage(options: MultiModalConversationParamOptions) = _aigc.createStreamMultiModalMessage(options)
}
