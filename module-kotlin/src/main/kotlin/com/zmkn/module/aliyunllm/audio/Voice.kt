package com.zmkn.module.aliyunllm.audio

import com.alibaba.dashscope.audio.ttsv2.enrollment.VoiceEnrollmentParam
import com.alibaba.dashscope.audio.ttsv2.enrollment.VoiceEnrollmentService
import com.alibaba.dashscope.common.Status
import com.alibaba.dashscope.exception.ApiException
import com.alibaba.dashscope.utils.Constants
import com.zmkn.module.aliyunllm.Base
import com.zmkn.module.aliyunllm.audio.extension.toResponseEnrolledVoice
import com.zmkn.module.aliyunllm.audio.model.*
import com.zmkn.module.aliyunllm.enumeration.ResponseCode
import com.zmkn.module.aliyunllm.model.ApiOptions
import com.zmkn.module.aliyunllm.model.RequestException
import com.zmkn.module.aliyunllm.util.AliyunLlmUtils
import com.zmkn.module.okhttp.NewOkHttpClient
import com.zmkn.module.okhttp.util.OkHttpUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Headers

class Voice(
    private val apiKeys: List<String>,
    apiOptions: ApiOptions? = null,
) : Base(
    apiKeys = apiKeys,
    apiOptions = apiOptions,
) {
    private val _httpClient: NewOkHttpClient = NewOkHttpClient(
        baseUrl = Constants.baseHttpApiUrl,
        okHttpClient = apiOptions?.connectionOptions?.let { connectionOptions ->
            OkHttpUtils.create(
                readTimeout = connectionOptions.readTimeout,
                writeTimeout = connectionOptions.writeTimeout,
                connectTimeout = connectionOptions.connectTimeout,
                maxIdleConnections = connectionOptions.connectionPoolSize,
            )
        },
    )

    private suspend fun <T> catch(
        apiKeyIndex: Int,
        block: suspend (apiKey: String) -> T,
        exceptionHandler: suspend (e: RequestException) -> T,
    ): T = try {
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

    private suspend fun createVoice(
        apiKeyIndex: Int,
        options: CreateVoiceOptions,
    ): ResponseCreatedVoice = withContext(Dispatchers.IO) {
        catch(
            apiKeyIndex,
            { apiKey ->
                val headers = Headers.headersOf("Authorization", "Bearer $apiKey")
                val response = _httpClient.post("/services/audio/tts/customization", options, headers)
                val responseBody = response.body.string()
                when (response.code) {
                    200 -> {
                        OkHttpUtils.decodeFromString(ResponseCreatedVoice::class, responseBody)
                    }

                    else -> {
                        val exception = OkHttpUtils.decodeFromString(RequestCreateVoiceException::class, responseBody)
                        throw ApiException(Status.builder().statusCode(response.code).code(exception.code).message(exception.message).build())
                    }
                }
            },
            { e ->
                throw e
            },
        )
    }

    private suspend fun enrollVoice(
        apiKeyIndex: Int,
        options: EnrollVoiceOptions,
    ): ResponseEnrolledVoice = withContext(Dispatchers.IO) {
        catch(
            apiKeyIndex,
            { apiKey ->
                val customParam = VoiceEnrollmentParam.builder()
                    .model("")
                    .languageHints(options.languageHints?.map { it.value })
                    .parameters(mapOf("enable_preprocess" to true))
                    .build()
                VoiceEnrollmentService(apiKey).createVoice(options.model, options.prefix, options.url, customParam).toResponseEnrolledVoice()
            },
            { e ->
                throw e
            },
        )
    }

    private suspend fun queryAllEnrolledVoices(
        apiKeyIndex: Int,
        prefix: String,
        pageIndex: Int,
        pageSize: Int,
    ): List<ResponseEnrolledVoice> = withContext(Dispatchers.IO) {
        catch(
            apiKeyIndex,
            { apiKey ->
                VoiceEnrollmentService(apiKey).listVoice(prefix, pageIndex, pageSize).toList().map { it.toResponseEnrolledVoice() }
            },
            { e ->
                throw e
            },
        )
    }

    private suspend fun queryEnrolledVoice(
        apiKeyIndex: Int,
        id: String,
    ): ResponseEnrolledVoice? = withContext(Dispatchers.IO) {
        catch(
            apiKeyIndex,
            { apiKey ->
                VoiceEnrollmentService(apiKey).queryVoice(id).toResponseEnrolledVoice()
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

    private suspend fun updateEnrolledVoice(
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

    private suspend fun deleteEnrolledVoice(
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

    suspend fun createVoice(options: CreateVoiceOptions) = createVoice(0, options)

    suspend fun enrollVoice(options: EnrollVoiceOptions) = enrollVoice(0, options)

    suspend fun queryAllEnrolledVoices(
        prefix: String,
        pageIndex: Int,
        pageSize: Int,
    ) = queryAllEnrolledVoices(0, prefix, pageIndex, pageSize)

    suspend fun queryEnrolledVoice(id: String) = queryEnrolledVoice(0, id)

    suspend fun updateEnrolledVoice(
        id: String,
        url: String,
    ) = updateEnrolledVoice(0, id, url)

    suspend fun deleteEnrolledVoice(id: String) = deleteEnrolledVoice(0, id)
}
