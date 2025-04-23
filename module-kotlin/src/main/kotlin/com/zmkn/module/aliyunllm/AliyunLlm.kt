package com.zmkn.module.aliyunllm

import com.zmkn.module.aliyunllm.aigc.Aigc
import com.zmkn.module.aliyunllm.aigc.model.GenerationParamOptions
import com.zmkn.module.aliyunllm.aigc.model.MultiModalConversationParamOptions
import com.zmkn.module.aliyunllm.audio.Audio
import com.zmkn.module.aliyunllm.audio.Voice
import com.zmkn.module.aliyunllm.audio.model.SpeechSynthesisParamOptions
import com.zmkn.module.aliyunllm.audio.model.VoiceEnrollmentCreateOptions
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

    private val _voice = Voice(
        apiKeys = apiKeys,
        apiOptions = null,
    )

    fun createStreamMessage(options: GenerationParamOptions) = _aigc.createStreamMessage(options)

    fun createStreamMultiModalMessage(options: MultiModalConversationParamOptions) = _aigc.createStreamMultiModalMessage(options)

    fun createStreamSpeechSynthesizer(options: SpeechSynthesisParamOptions) = _audio.createStreamSpeechSynthesizer(options)

    suspend fun createVoice(options: VoiceEnrollmentCreateOptions) = _voice.createVoice(options)

    suspend fun queryAllVoices(
        prefix: String,
        pageIndex: Int,
        pageSize: Int,
    ) = _voice.queryAllVoices(
        prefix = prefix,
        pageIndex = pageIndex,
        pageSize = pageSize,
    )

    suspend fun queryVoice(id: String) = _voice.queryVoice(id)

    suspend fun updateVoice(
        id: String,
        url: String,
    ) = _voice.updateVoice(
        id = id,
        url = url,
    )

    suspend fun deleteVoice(id: String) = _voice.deleteVoice(id)
}
