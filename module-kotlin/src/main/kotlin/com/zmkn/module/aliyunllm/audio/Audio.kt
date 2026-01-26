package com.zmkn.module.aliyunllm.audio

import com.alibaba.dashscope.audio.tts.SpeechSynthesisResult
import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesisParam
import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesizer
import com.alibaba.dashscope.common.ResultCallback
import com.zmkn.module.aliyunllm.Base
import com.zmkn.module.aliyunllm.audio.enumeration.ResponseCode
import com.zmkn.module.aliyunllm.audio.extension.toResponseSpeechSynthesis
import com.zmkn.module.aliyunllm.audio.extension.toSpeechSynthesisAudioFormat
import com.zmkn.module.aliyunllm.audio.extension.toSpeechSynthesisTextType
import com.zmkn.module.aliyunllm.audio.model.RequestException
import com.zmkn.module.aliyunllm.audio.model.ResponseSpeechSynthesis
import com.zmkn.module.aliyunllm.audio.model.SpeechSynthesisParamOptions
import com.zmkn.module.aliyunllm.audio.model.SpeechSynthesisParamOptions.TextType
import com.zmkn.module.aliyunllm.audio.util.AudioUtils
import com.zmkn.module.aliyunllm.model.ApiOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

class Audio(
    private val apiKeys: List<String>,
    speechSynthesizerObjectPoolSize: Int?,
    apiOptions: ApiOptions?,
) : Base(
    apiKeys = apiKeys,
    apiOptions = apiOptions,
) {
    constructor(
        apiKeys: List<String>,
        speechSynthesizerObjectPoolSize: Int?,
    ) : this(
        apiKeys = apiKeys,
        speechSynthesizerObjectPoolSize = speechSynthesizerObjectPoolSize,
        apiOptions = null,
    )

    constructor(
        apiKeys: List<String>,
        apiOptions: ApiOptions?,
    ) : this(
        apiKeys = apiKeys,
        speechSynthesizerObjectPoolSize = null,
        apiOptions = apiOptions,
    )

    constructor(
        apiKeys: List<String>,
    ) : this(
        apiKeys = apiKeys,
        speechSynthesizerObjectPoolSize = null,
        apiOptions = null,
    )

    private val _speechSynthesizerObjectPool: SpeechSynthesizerObjectPool = SpeechSynthesizerObjectPool(speechSynthesizerObjectPoolSize ?: OBJECT_POOL_SIZE)

    private fun createSpeechSynthesisParam(
        apiKeyIndex: Int,
        options: SpeechSynthesisParamOptions
    ): SpeechSynthesisParam = SpeechSynthesisParam
        .builder()
        .apiKey(getApiKey(apiKeyIndex))
        .model(options.model)
        .voice(options.voice)
        .apply {
            options.textType?.also {
                textType(it.toSpeechSynthesisTextType())
            }
            options.format?.also {
                format(it.toSpeechSynthesisAudioFormat())
            }
            options.volume?.also {
                volume(it)
            }
            options.speechRate?.also {
                speechRate(it)
            }
            options.pitchRate?.also {
                pitchRate(it)
            }
            options.bitRate?.also {
                parameter("bit_rate", it)
            }
            options.enableWordTimestamp?.also {
                enableWordTimestamp(it)
            }
            options.enablePhonemeTimestamp?.also {
                enablePhonemeTimestamp(it)
            }
            options.seed?.also {
                seed(it)
            }
            options.languageHints?.also {
                languageHints(it.map { languageHint ->
                    languageHint.value
                })
            }
            options.instruction?.also {
                instruction(it)
            }
            options.enableAigcTag?.also {
                parameter("enable_aigc_tag", it)
            }
            options.aigcPropagator?.also {
                parameter("aigc_propagator", it)
            }
            options.aigcPropagateId?.also {
                parameter("aigc_propagate_id", it)
            }
        }.build()

    private fun createStreamSpeechSynthesizer(
        apiKeyIndex: Int,
        speechSynthesizer: SpeechSynthesizer,
        options: SpeechSynthesisParamOptions
    ): Flow<ResponseSpeechSynthesis> = channelFlow {
        val param = createSpeechSynthesisParam(apiKeyIndex, options)
        val resultCallback = object : ResultCallback<SpeechSynthesisResult>() {
            override fun onEvent(result: SpeechSynthesisResult) {
                if (result.usage != null || result.audioFrame != null) {
                    trySend(result.toResponseSpeechSynthesis())
                }
            }

            override fun onComplete() {
                close()
            }

            override fun onError(e: Exception) {
                handleStreamSpeechSynthesizerError(e, apiKeyIndex, speechSynthesizer, options)
            }
        }
        speechSynthesizer.updateParamAndCallback(param, resultCallback)
        if (options.textType == TextType.SSML) {
            speechSynthesizer.call(options.texts[0])
        } else {
            AudioUtils.formatSpeechSynthesizerTexts(options.texts).forEach { text ->
                speechSynthesizer.streamingCall(text)
            }
            speechSynthesizer.streamingComplete()
        }
        awaitClose()
    }

    private fun createStreamSpeechSynthesizer(
        apiKeyIndex: Int,
        options: SpeechSynthesisParamOptions
    ): Flow<ResponseSpeechSynthesis> = channelFlow {
        val speechSynthesizer = _speechSynthesizerObjectPool.pool.borrowObject()
        launch(Dispatchers.IO) {
            createStreamSpeechSynthesizer(
                apiKeyIndex = apiKeyIndex,
                speechSynthesizer = speechSynthesizer,
                options = options,
            ).catch {
                close(it)
            }.collect {
                send(it)
            }
            close()
        }
        awaitClose {
            runCatching {
                // 确保在通道关闭时归还资源
                _speechSynthesizerObjectPool.pool.returnObject(speechSynthesizer)
            }
        }
    }

    private fun ProducerScope<ResponseSpeechSynthesis>.handleStreamSpeechSynthesizerError(
        e: Exception,
        apiKeyIndex: Int,
        speechSynthesizer: SpeechSynthesizer,
        options: SpeechSynthesisParamOptions,
    ) {
        val requestException = RequestException(e)
        val responseCode = requestException.responseCode
        if (
            ((responseCode.statusCode == ResponseCode.INVALID_API_KEY.statusCode && responseCode.code == ResponseCode.INVALID_API_KEY.code) || (responseCode.statusCode == ResponseCode.MODEL_ACCESS_DENIED.statusCode && responseCode.code == ResponseCode.MODEL_ACCESS_DENIED.code)) &&
            apiKeyIndex + 1 < apiKeys.size
        ) {
            launch(Dispatchers.IO) {
                createStreamSpeechSynthesizer(apiKeyIndex + 1, speechSynthesizer, options)
                    .catch {
                        close(it)
                    }
                    .collect { response ->
                        send(response)
                    }
                close()
            }
        } else {
            close(requestException)
        }
    }

    fun createStreamSpeechSynthesizer(options: SpeechSynthesisParamOptions) = createStreamSpeechSynthesizer(0, options)

    companion object {
        const val OBJECT_POOL_SIZE: Int = 500
    }
}
