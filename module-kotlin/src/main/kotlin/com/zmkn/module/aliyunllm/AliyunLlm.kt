package com.zmkn.module.aliyunllm

import com.zmkn.module.aliyunllm.aigc.Aigc
import com.zmkn.module.aliyunllm.aigc.model.GenerationParamOptions
import com.zmkn.module.aliyunllm.aigc.model.MultiModalConversationParamOptions
import com.zmkn.module.aliyunllm.audio.Audio
import com.zmkn.module.aliyunllm.audio.model.SpeechSynthesisParamOptions
import com.zmkn.module.aliyunllm.model.ApiOptions

class AliyunLlm : Base {
    constructor(
        apiKeys: List<String>,
        apiOptions: ApiOptions?,
    ) : super(
        apiKeys = apiKeys,
        apiOptions = apiOptions,
    ) {
        _aigc = Aigc(
            apiKeys = apiKeys,
            apiOptions = null,
        )
        _audio = Audio(
            apiKeys = apiKeys,
            apiOptions = null,
        )
    }

    constructor(
        apiKeys: List<String>,
    ) : this(
        apiKeys = apiKeys,
        apiOptions = null,
    )

    private val _aigc: Aigc
    private val _audio: Audio

    fun createStreamMessage(options: GenerationParamOptions) = _aigc.createStreamMessage(options)

    fun createStreamMultiModalMessage(options: MultiModalConversationParamOptions) = _aigc.createStreamMultiModalMessage(options)

    fun createStreamSpeechSynthesizer(options: SpeechSynthesisParamOptions) = _audio.createStreamSpeechSynthesizer(options)
}
