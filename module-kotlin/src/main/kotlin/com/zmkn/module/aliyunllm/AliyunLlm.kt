package com.zmkn.module.aliyunllm

import com.zmkn.module.aliyunllm.aigc.Aigc
import com.zmkn.module.aliyunllm.aigc.model.GenerationParamOptions
import com.zmkn.module.aliyunllm.aigc.model.MultiModalConversationParamOptions
import com.zmkn.module.aliyunllm.audio.Audio
import com.zmkn.module.aliyunllm.audio.model.AudioOptions
import com.zmkn.module.aliyunllm.audio.model.SpeechSynthesisParamOptions

class AliyunLlm(
    apiKeys: List<String>,
    audioOptions: AudioOptions = AudioOptions(),
) : Base(apiKeys) {
    private val _aigc = Aigc(apiKeys)
    private val _audio = Audio(apiKeys, audioOptions)

    fun createStreamMessage(options: GenerationParamOptions) = _aigc.createStreamMessage(options)

    fun createStreamMultiModalMessage(options: MultiModalConversationParamOptions) = _aigc.createStreamMultiModalMessage(options)

    fun createStreamSpeechSynthesizer(options: SpeechSynthesisParamOptions) = _audio.createStreamSpeechSynthesizer(options)
}
