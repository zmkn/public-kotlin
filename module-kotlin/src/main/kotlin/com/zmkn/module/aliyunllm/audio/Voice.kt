package com.zmkn.module.aliyunllm.audio

import com.alibaba.dashscope.audio.ttsv2.enrollment.VoiceEnrollmentService
import com.zmkn.module.aliyunllm.Base
import com.zmkn.module.aliyunllm.audio.extension.toResponseVoice
import com.zmkn.module.aliyunllm.audio.model.ResponseVoice
import com.zmkn.module.aliyunllm.audio.model.VoiceEnrollmentCreateOptions
import com.zmkn.module.aliyunllm.enumeration.ResponseCode
import com.zmkn.module.aliyunllm.model.ApiOptions
import com.zmkn.module.aliyunllm.model.RequestException
import com.zmkn.module.aliyunllm.util.AliyunLlmUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Voice(
    private val apiKeys: List<String>,
    apiOptions: ApiOptions? = null,
) : Base(
    apiKeys = apiKeys,
    apiOptions = apiOptions,
) {
    private suspend fun <T> catch(
        apiKeyIndex: Int,
        block: suspend (apiKey: String) -> T,
        exceptionHandler: suspend (e: RequestException) -> T,
    ): T {
        return try {
            block(getApiKey(apiKeyIndex))
        } catch (e: Exception) {
            val requestException = RequestException(e)
            val responseCode = requestException.responseCode
            if (responseCode.statusCode == ResponseCode.INVALID_API_KEY.statusCode && responseCode.code == ResponseCode.INVALID_API_KEY.code && apiKeyIndex + 1 < apiKeys.size) {
                catch(apiKeyIndex + 1, block, exceptionHandler)
            } else {
                exceptionHandler(requestException)
            }
        }
    }

    private suspend fun createVoice(
        apiKeyIndex: Int,
        options: VoiceEnrollmentCreateOptions,
    ): ResponseVoice = withContext(Dispatchers.IO) {
        catch(
            apiKeyIndex,
            { apiKey ->
                VoiceEnrollmentService(apiKey).createVoice(options.model, options.prefix, options.url).toResponseVoice()
            },
            { e ->
                throw e
            },
        )
    }

    private suspend fun queryAllVoices(
        apiKeyIndex: Int,
        prefix: String,
        pageIndex: Int,
        pageSize: Int,
    ): List<ResponseVoice> = withContext(Dispatchers.IO) {
        catch(
            apiKeyIndex,
            { apiKey ->
                VoiceEnrollmentService(apiKey).listVoice(prefix, pageIndex, pageSize).toList().map { it.toResponseVoice() }
            },
            { e ->
                throw e
            },
        )
    }

    private suspend fun queryVoice(
        apiKeyIndex: Int,
        id: String,
    ): ResponseVoice? = withContext(Dispatchers.IO) {
        catch(
            apiKeyIndex,
            { apiKey ->
                VoiceEnrollmentService(apiKey).queryVoice(id).toResponseVoice()
            },
            { e ->
                val responseCode = e.responseCode
                if (responseCode.statusCode == ResponseCode.BAD_REQUEST_RESOURCE_NOT_EXIST.statusCode && responseCode.code == ResponseCode.BAD_REQUEST_RESOURCE_NOT_EXIST.code) {
                    null
                } else {
                    throw e
                }
            },
        )
    }

    private suspend fun updateVoice(
        apiKeyIndex: Int,
        id: String,
        url: String,
    ): Boolean = withContext(Dispatchers.IO) {
        catch(
            apiKeyIndex,
            { apiKey ->
                if (!AliyunLlmUtils.isUrl(url)) {
                    throw IllegalArgumentException("url must be a valid url address.")
                }
                VoiceEnrollmentService(apiKey).updateVoice(id, url)
                true
            },
            { e ->
                val responseCode = e.responseCode
                if (responseCode.statusCode == ResponseCode.BAD_REQUEST_RESOURCE_NOT_EXIST.statusCode && responseCode.code == ResponseCode.BAD_REQUEST_RESOURCE_NOT_EXIST.code) {
                    false
                } else {
                    throw e
                }
            },
        )
    }

    private suspend fun deleteVoice(
        apiKeyIndex: Int,
        id: String,
    ): Boolean = withContext(Dispatchers.IO) {
        catch(
            apiKeyIndex,
            { apiKey ->
                VoiceEnrollmentService(apiKey).deleteVoice(id)
                true
            },
            { e ->
                val responseCode = e.responseCode
                if (responseCode.statusCode == ResponseCode.BAD_REQUEST_RESOURCE_NOT_EXIST.statusCode && responseCode.code == ResponseCode.BAD_REQUEST_RESOURCE_NOT_EXIST.code) {
                    false
                } else {
                    throw e
                }
            },
        )
    }

    suspend fun createVoice(options: VoiceEnrollmentCreateOptions) = createVoice(0, options)

    suspend fun queryAllVoices(
        prefix: String,
        pageIndex: Int,
        pageSize: Int,
    ) = queryAllVoices(0, prefix, pageIndex, pageSize)

    suspend fun queryVoice(id: String) = queryVoice(0, id)

    suspend fun updateVoice(
        id: String,
        url: String,
    ) = updateVoice(0, id, url)

    suspend fun deleteVoice(id: String) = deleteVoice(0, id)
}
