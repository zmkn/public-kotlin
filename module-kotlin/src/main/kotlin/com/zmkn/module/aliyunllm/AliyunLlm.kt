package com.zmkn.module.aliyunllm

import com.zmkn.module.aliyunllm.aigc.Aigc
import com.zmkn.module.aliyunllm.aigc.model.GenerationParamOptions
import com.zmkn.module.aliyunllm.aigc.model.MultiModalConversationParamOptions
import com.zmkn.module.aliyunllm.audio.Audio
import com.zmkn.module.aliyunllm.audio.model.SpeechSynthesisParamOptions
import com.zmkn.module.aliyunllm.model.ApiOptions

class AliyunLlm(
    apiKeys: List<String>,
    speechSynthesizerObjectPoolSize: Int? = null,
    apiOptions: ApiOptions? = null,
) : Base(
    apiKeys = apiKeys,
    apiOptions = apiOptions,
) {
    private val _aigc = Aigc(
        apiKeys = apiKeys,
        apiOptions = null,
    )

    private val _audio = Audio(
        apiKeys = apiKeys,
        speechSynthesizerObjectPoolSize = speechSynthesizerObjectPoolSize,
        apiOptions = null,
    )

    fun createStreamMessage(options: GenerationParamOptions) = _aigc.createStreamMessage(options)

    fun createStreamMultiModalMessage(options: MultiModalConversationParamOptions) = _aigc.createStreamMultiModalMessage(options)

    fun createStreamSpeechSynthesizer(options: SpeechSynthesisParamOptions) = _audio.createStreamSpeechSynthesizer(options)
}
