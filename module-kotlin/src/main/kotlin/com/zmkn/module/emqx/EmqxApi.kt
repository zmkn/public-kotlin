package com.zmkn.module.emqx

import com.zmkn.module.emqx.exception.EmqxResponseUnknownException
import com.zmkn.module.emqx.extension.toEmqxPublishResponseException
import com.zmkn.module.emqx.extension.toEmqxResponseException
import com.zmkn.module.emqx.model.*
import com.zmkn.module.okhttp.NewOkHttpClient
import com.zmkn.module.okhttp.listener.BaseUrlInterceptRequestListener
import com.zmkn.module.okhttp.listener.BasicAuthInterceptRequestListener
import com.zmkn.module.okhttp.util.OkHttpUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import kotlin.reflect.typeOf

class EmqxApi(
    private val config: Config,
    private val newOkHttpClient: NewOkHttpClient = NewOkHttpClient(baseUrl = config.baseUrl)
) {
    init {
        newOkHttpClient.okHttpClient = createOkHttpClient(newOkHttpClient.okHttpClient, config.baseUrl, config.key, config.secret)
    }

    suspend fun getClients(): ListResponseBody<ClientResponseBody, PaginationMeta> {
        val response = newOkHttpClient.get(
            url = "/clients"
        )
        return if (response.body != null) {
            val responseBody = try {
                withContext(Dispatchers.IO) {
                    response.body!!.string()
                }
            } catch (_: Exception) {
                throw EmqxResponseUnknownException()
            }
            when (response.code) {
                200 -> {
                    val kType = typeOf<ListResponseBody<ClientResponseBody, PaginationMeta>>()
                    val newResponseBody = responseBody.replace(Regex("\"infinity\""), "null")
                    OkHttpUtils.decodeFromString<ListResponseBody<ClientResponseBody, PaginationMeta>>(kType, newResponseBody)
                }

                400 -> {
                    throw OkHttpUtils.decodeFromString(ExceptionResponseBody::class, responseBody).toEmqxResponseException()
                }

                else -> {
                    throw EmqxResponseUnknownException(responseBody)
                }
            }.apply {
                response.close()
            }
        } else {
            throw EmqxResponseUnknownException()
        }
    }

    suspend fun publish(body: PublishRequestBody): PublishResponseBody {
        val response = newOkHttpClient.post(
            url = "/publish",
            body = body
        )
        return if (response.body != null) {
            val responseBody = try {
                withContext(Dispatchers.IO) {
                    response.body!!.string()
                }
            } catch (_: Exception) {
                throw EmqxResponseUnknownException()
            }
            when (response.code) {
                200 -> {
                    OkHttpUtils.decodeFromString(PublishResponseBody::class, responseBody)
                }

                202, 503 -> {
                    throw OkHttpUtils.decodeFromString(PublishExceptionResponseBody::class, responseBody).toEmqxPublishResponseException()
                }

                400 -> {
                    throw OkHttpUtils.decodeFromString(ExceptionResponseBody::class, responseBody).toEmqxResponseException()
                }

                else -> {
                    throw EmqxResponseUnknownException(responseBody)
                }
            }.apply {
                response.close()
            }
        } else {
            throw EmqxResponseUnknownException()
        }
    }

    companion object {
        private fun createOkHttpClient(
            okHttpClient: OkHttpClient,
            baseUrl: String,
            username: String,
            password: String
        ): OkHttpClient {
            val interceptor = OkHttpUtils.createInterceptor(
                interceptRequestListeners = listOf(
                    BasicAuthInterceptRequestListener(username, password),
                    BaseUrlInterceptRequestListener(baseUrl),
                )
            )
            return okHttpClient.newBuilder()
                .addInterceptor(interceptor)
                .build()
        }
    }
}
