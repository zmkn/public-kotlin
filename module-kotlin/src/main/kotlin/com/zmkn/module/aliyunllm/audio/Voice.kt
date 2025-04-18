package com.zmkn.module.aliyunllm.audio

import com.alibaba.dashscope.audio.ttsv2.enrollment.VoiceEnrollmentService
import com.zmkn.module.aliyunllm.Base
import com.zmkn.module.aliyunllm.audio.extension.toResponseVoice
import com.zmkn.module.aliyunllm.audio.model.ResponseVoice
import com.zmkn.module.aliyunllm.audio.model.VoiceEnrollmentCreateOptions
import com.zmkn.module.aliyunllm.enumeration.ResponseCode
import com.zmkn.module.aliyunllm.model.ApiOptions
import com.zmkn.module.aliyunllm.model.RequestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Voice(
    private val apiKeys: List<String>,
    apiOptions: ApiOptions? = null,
) : Base(
    apiKeys = apiKeys,
    apiOptions = apiOptions,
) {
    private suspend fun createVoice(
        apiKeyIndex: Int,
        options: VoiceEnrollmentCreateOptions,
    ): ResponseVoice = withContext(Dispatchers.IO) {
        try {
            VoiceEnrollmentService(getApiKey(apiKeyIndex)).createVoice(options.model, options.prefix, options.url).toResponseVoice()
        } catch (e: Exception) {
            val requestException = RequestException(e)
            val responseCode = requestException.responseCode
            if (responseCode.statusCode == ResponseCode.INVALID_API_KEY.statusCode && responseCode.code == ResponseCode.INVALID_API_KEY.code && apiKeyIndex + 1 < apiKeys.size) {
                createVoice(apiKeyIndex + 1, options)
            } else {
                throw requestException
            }
        }
    }

    private suspend fun queryAll(
        apiKeyIndex: Int,
        prefix: String,
        pageIndex: Int,
        pageSize: Int,
    ): List<ResponseVoice> = withContext(Dispatchers.IO) {
        try {
            VoiceEnrollmentService(getApiKey(apiKeyIndex)).listVoice(prefix, pageIndex, pageSize).toList().map { it.toResponseVoice() }
        } catch (e: Exception) {
            val requestException = RequestException(e)
            val responseCode = requestException.responseCode
            if (responseCode.statusCode == ResponseCode.INVALID_API_KEY.statusCode && responseCode.code == ResponseCode.INVALID_API_KEY.code && apiKeyIndex + 1 < apiKeys.size) {
                queryAll(apiKeyIndex + 1, prefix, pageIndex, pageSize)
            } else {
                throw requestException
            }
        }
    }

    suspend fun createVoice(options: VoiceEnrollmentCreateOptions) = createVoice(0, options)

    suspend fun queryAll(
        prefix: String,
        pageIndex: Int,
        pageSize: Int,
    ) = queryAll(0, prefix, pageIndex, pageSize)
}
