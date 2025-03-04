package com.zmkn.module.okhttp

import com.zmkn.module.okhttp.extension.*
import com.zmkn.module.okhttp.util.OkHttpUtils
import com.zmkn.module.okhttp.util.OkHttpUtils.EMPTY_JSON
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import kotlin.reflect.KType

class NewOkHttpClient(
    var okHttpClient: OkHttpClient = OkHttpUtils.create(),
    val baseUrl: String,
) {
    suspend fun request(request: Request): Response {
        val url = request.url.toString()
        val newRequest = if (OkHttpUtils.isFullUrl(url)) {
            request
        } else {
            request.newBuilder()
                .url(OkHttpUtils.createFullUrl(baseUrl, url))
                .build()
        }
        return okHttpClient.request(newRequest)
    }

    suspend fun get(
        url: String,
        queryParams: Map<String, String?> = emptyMap(),
        headers: Headers? = null,
    ): Response {
        val fullUrl = OkHttpUtils.createFullUrl(baseUrl, url)
        return okHttpClient.get(fullUrl, queryParams, headers)
    }

    suspend fun delete(
        url: String,
        queryParams: Map<String, String?> = emptyMap(),
        headers: Headers? = null,
    ): Response {
        val fullUrl = OkHttpUtils.createFullUrl(baseUrl, url)
        return okHttpClient.delete(fullUrl, queryParams, headers)
    }

    suspend fun post(
        url: String,
        json: String = EMPTY_JSON,
        headers: Headers? = null,
    ): Response {
        val fullUrl = OkHttpUtils.createFullUrl(baseUrl, url)
        return okHttpClient.post(fullUrl, json, headers)
    }

    suspend inline fun <reified T : Any> post(
        url: String,
        body: T,
        headers: Headers? = null,
    ): Response {
        val fullUrl = OkHttpUtils.createFullUrl(baseUrl, url)
        return okHttpClient.post(fullUrl, body, headers)
    }

    suspend fun <T : Any> post(
        url: String,
        kType: KType,
        body: T,
        headers: Headers? = null,
    ): Response {
        val fullUrl = OkHttpUtils.createFullUrl(baseUrl, url)
        return okHttpClient.post(fullUrl, kType, body, headers)
    }

    suspend fun post(
        url: String,
        formData: Map<String, String>,
        headers: Headers? = null,
    ): Response {
        val fullUrl = OkHttpUtils.createFullUrl(baseUrl, url)
        return okHttpClient.post(fullUrl, formData, headers)
    }

    suspend fun put(
        url: String,
        json: String = EMPTY_JSON,
        headers: Headers? = null,
    ): Response {
        val fullUrl = OkHttpUtils.createFullUrl(baseUrl, url)
        return okHttpClient.put(fullUrl, json, headers)
    }

    suspend inline fun <reified T : Any> put(
        url: String,
        body: T,
        headers: Headers? = null,
    ): Response {
        val fullUrl = OkHttpUtils.createFullUrl(baseUrl, url)
        return okHttpClient.put(fullUrl, body, headers)
    }

    suspend fun <T : Any> put(
        url: String,
        kType: KType,
        body: T,
        headers: Headers? = null,
    ): Response {
        val fullUrl = OkHttpUtils.createFullUrl(baseUrl, url)
        return okHttpClient.put(fullUrl, kType, body, headers)
    }

    suspend fun put(
        url: String,
        formData: Map<String, String>,
        headers: Headers? = null,
    ): Response {
        val fullUrl = OkHttpUtils.createFullUrl(baseUrl, url)
        return okHttpClient.put(fullUrl, formData, headers)
    }
}
