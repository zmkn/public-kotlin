package com.zmkn.module.aliyunllm.audio

import com.alibaba.dashscope.audio.tts.SpeechSynthesisResult
import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesisParam
import com.alibaba.dashscope.common.ResultCallback
import com.zmkn.module.aliyunllm.Base
import com.zmkn.module.aliyunllm.audio.enumeration.ResponseCode
import com.zmkn.module.aliyunllm.audio.extension.toResponseSpeechSynthesis
import com.zmkn.module.aliyunllm.audio.extension.toSpeechSynthesisAudioFormat
import com.zmkn.module.aliyunllm.audio.extension.toSpeechSynthesisTextType
import com.zmkn.module.aliyunllm.audio.model.RequestException
import com.zmkn.module.aliyunllm.audio.model.ResponseSpeechSynthesis
import com.zmkn.module.aliyunllm.audio.model.SpeechSynthesisParamOptions
import com.zmkn.module.aliyunllm.audio.util.AudioUtils
import com.zmkn.module.aliyunllm.model.ApiOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

class Audio : Base {
    constructor(
        apiKeys: List<String>,
        speechSynthesizerObjectPoolSize: Int?,
        apiOptions: ApiOptions?,
    ) : super(
        apiKeys = apiKeys,
        apiOptions = apiOptions,
    ) {
        _apiKeys = apiKeys
        _speechSynthesizerObjectPool = SpeechSynthesizerObjectPool(speechSynthesizerObjectPoolSize ?: OBJECT_POOL_SIZE)
    }

    constructor(
        apiKeys: List<String>,
        objectPoolSize: Int?,
    ) : this(
        apiKeys = apiKeys,
        speechSynthesizerObjectPoolSize = objectPoolSize,
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

    private val _apiKeys: List<String>
    private val _speechSynthesizerObjectPool: SpeechSynthesizerObjectPool

    private fun createSpeechSynthesisParam(
        apiKeyIndex: Int,
        options: SpeechSynthesisParamOptions
    ): SpeechSynthesisParam {
        return SpeechSynthesisParam
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
                options.enableWordTimestamp?.also {
                    enableWordTimestamp(it)
                }
                options.enablePhonemeTimestamp?.also {
                    enablePhonemeTimestamp(it)
                }
            }.build()
    }

    private fun createStreamSpeechSynthesizer(
        apiKeyIndex: Int,
        options: SpeechSynthesisParamOptions
    ): Flow<ResponseSpeechSynthesis> = channelFlow {
        launch(Dispatchers.IO) {
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
                    val requestException = RequestException(e)
                    val responseCode = requestException.responseCode
                    if (
                        ((responseCode.statusCode == ResponseCode.INVALID_API_KEY.statusCode && responseCode.code == ResponseCode.INVALID_API_KEY.code)
                                || (responseCode.statusCode == ResponseCode.MODEL_ACCESS_DENIED.statusCode && responseCode.code == ResponseCode.MODEL_ACCESS_DENIED.code))
                        && apiKeyIndex + 1 < _apiKeys.size
                    ) {
                        launch(Dispatchers.IO) {
                            createStreamSpeechSynthesizer(apiKeyIndex + 1, options)
                                .catch {
                                    close(it)
                                }.collect {
                                    send(it)
                                }
                            close()
                        }
                    } else {
                        close(requestException)
                    }
                }
            }
            val speechSynthesizer = _speechSynthesizerObjectPool.pool.borrowObject()
            speechSynthesizer.updateParamAndCallback(param, resultCallback)
            AudioUtils.formatSpeechSynthesizerTexts(options.texts).forEach { text ->
                speechSynthesizer.streamingCall(text)
            }
            speechSynthesizer.streamingComplete()
            _speechSynthesizerObjectPool.pool.returnObject(speechSynthesizer)
        }
        awaitClose()
    }

    fun createStreamSpeechSynthesizer(options: SpeechSynthesisParamOptions) = createStreamSpeechSynthesizer(0, options)

    companion object {
        const val OBJECT_POOL_SIZE: Int = 500
    }
}
