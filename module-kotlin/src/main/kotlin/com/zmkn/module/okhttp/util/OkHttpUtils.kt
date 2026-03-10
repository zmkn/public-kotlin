package com.zmkn.module.okhttp.util

import com.zmkn.module.okhttp.enumeration.MediaType
import com.zmkn.module.okhttp.interfaces.InterceptRequestListener
import com.zmkn.module.okhttp.interfaces.InterceptResponseListener
import com.zmkn.service.SerializationService.Companion.json
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.internal.FormatLanguage
import kotlinx.serialization.serializer
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MultipartBody.Part
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.net.URI
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType

object OkHttpUtils {
    const val EMPTY_JSON: String = "{}"

    fun create(
        followRedirects: Boolean? = null,
        followSslRedirects: Boolean? = null,
        readTimeout: Long? = null,
        writeTimeout: Long? = null,
        connectTimeout: Long? = null,
        callTimeout: Long? = null,
        retryOnConnectionFailure: Boolean? = null,
        maxIdleConnections: Int? = null,
        keepAliveDuration: Long? = null,
        protocols: List<Protocol>? = null,
    ): OkHttpClient = OkHttpClient.Builder()
        // 配置此客户端以遵循重定向
        .followRedirects(followRedirects ?: true)
        // 配置此客户端以允许协议从 HTTPS 重定向到 HTTP 以及从 HTTP 重定向到 HTTPS。重定向仍然首先受 followRedirects 限制。默认为 true。
        .followSslRedirects(followSslRedirects ?: true)
        // 设置读取数据超时时间为60秒
        .readTimeout(readTimeout ?: 60, TimeUnit.SECONDS)
        // 设置写入数据超时时间为60秒
        .writeTimeout(writeTimeout ?: 60, TimeUnit.SECONDS)
        // 设置建立连接超时时间为10秒
        .connectTimeout(connectTimeout ?: 10, TimeUnit.SECONDS)
        // 设置整体请求超时时间，默认0为不超时。
        .callTimeout(callTimeout ?: 0, TimeUnit.SECONDS)
        // 设置连接失败时不自动重试
        .retryOnConnectionFailure(retryOnConnectionFailure ?: true)
        // 设置连接池最大连接数为100个，每个连接最长存活时间为5分钟
        .connectionPool(ConnectionPool(maxIdleConnections ?: 100, keepAliveDuration ?: 5, TimeUnit.MINUTES))
        // 设置连接协议列表
        .protocols(protocols ?: listOf(Protocol.HTTP_2, Protocol.HTTP_1_1))
        .build()

    fun isFullUrl(url: String): Boolean = !url.startsWith("/")

    fun convertToStandardSchemeWithSubdomain(
        url: String,
        newScheme: String = "http",
    ): String {
        val uri = URI.create(url)
        val scheme = uri.scheme
        return if (scheme != "http" && scheme != "https") {
            URI(
                newScheme,
                uri.userInfo,
                "${uri.host}.$scheme",
                uri.port,
                uri.path,
                uri.query,
                uri.fragment,
            ).toString()
        } else {
            url
        }
    }

    fun createFullUrl(
        baseUrl: String,
        url: String,
    ): String = if (!isFullUrl(url)) {
        "$baseUrl$url"
    } else {
        url
    }.let {
        convertToStandardSchemeWithSubdomain(it)
    }

    fun createInterceptor(
        interceptRequestListeners: List<InterceptRequestListener> = emptyList(),
        interceptResponseListeners: List<InterceptResponseListener> = emptyList(),
    ): Interceptor = Interceptor { chain ->
        var request = chain.request()
        interceptRequestListeners.forEach {
            request = it.handler(chain, request)
        }
        if (interceptResponseListeners.isEmpty()) {
            chain.proceed(request)
        } else {
            var response = interceptResponseListeners.toMutableList().removeAt(0).handler(chain, request, null)
            interceptResponseListeners.forEach {
                response = it.handler(chain, request, response)
            }
            response
        }
    }

    fun createFormRequestBody(formData: Map<String, String>): FormBody = FormBody.Builder()
        .apply {
            for ((key, value) in formData) {
                add(key, value)
            }
        }
        .build()

    fun createJsonRequestBody(body: String): RequestBody = body.toRequestBody(MediaType.APPLICATION_JSON.value)

    fun createMultipartRequestBody(
        type: okhttp3.MediaType = MultipartBody.FORM,
        parts: List<Part> = emptyList(),
    ): MultipartBody = MultipartBody.Builder()
        .setType(type)
        .apply {
            parts.forEach {
                addPart(it)
            }
        }
        .build()

    fun createFormMultipartRequestBody(
        vararg parts: Part,
    ): MultipartBody = createMultipartRequestBody(
        type = MultipartBody.FORM,
        parts = parts.toList(),
    )

    fun createFormMultipartRequestBody(
        vararg parts: Pair<String, String>,
    ): MultipartBody = createFormMultipartRequestBody(
        *parts.map {
            Part.createFormData(it.first, it.second)
        }.toTypedArray()
    )

    fun createFileMultipartRequestBody(
        files: List<Pair<String, File>>,
        parts: List<Pair<String, String>> = emptyList(),
    ): RequestBody = createFormMultipartRequestBody(
        *files.map {
            Part.createFormData(it.first, it.second.name, it.second.asRequestBody(MultipartBody.FORM))
        }.plus(
            parts.map {
                Part.createFormData(it.first, it.second)
            }
        ).toTypedArray(),
    )

    fun createQueryHttpUrl(
        url: String,
        queryParams: Map<String, String?> = emptyMap(),
    ): HttpUrl = url.toHttpUrl().newBuilder()
        .apply {
            for ((key, value) in queryParams) {
                addQueryParameter(key, value)
            }
        }.build()

    fun createQueryHttpUrl(
        baseUrl: String,
        url: String,
        queryParams: Map<String, String?> = emptyMap(),
    ): HttpUrl = createQueryHttpUrl(createFullUrl(baseUrl, url), queryParams)

    inline fun <reified T> encodeToString(value: T): String = json.encodeToString(value)

    fun <T> encodeToString(kType: KType, value: T): String = json.encodeToString(json.serializersModule.serializer(kType), value)

    fun <T : Any> encodeToString(schema: KClass<T>, value: T): String = encodeToString(schema.starProjectedType, value)

    inline fun <reified T> decodeFromString(jsonString: String): T = json.decodeFromString<T>(jsonString)

    @Suppress("UNCHECKED_CAST")
    @OptIn(InternalSerializationApi::class)
    fun <T> decodeFromString(kType: KType, @FormatLanguage("json", "", "") jsonString: String): T = json.decodeFromString(json.serializersModule.serializer(kType), jsonString) as T

    @OptIn(InternalSerializationApi::class)
    fun <T : Any> decodeFromString(schema: KClass<T>, @FormatLanguage("json", "", "") jsonString: String): T = decodeFromString(schema.starProjectedType, jsonString)
}
