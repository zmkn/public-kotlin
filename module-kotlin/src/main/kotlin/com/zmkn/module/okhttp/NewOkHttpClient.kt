package com.zmkn.module.okhttp

import com.zmkn.module.okhttp.extension.*
import com.zmkn.module.okhttp.util.OkHttpUtils
import com.zmkn.module.okhttp.util.OkHttpUtils.EMPTY_JSON
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import kotlin.reflect.KType

class NewOkHttpClient(
    var okHttpClient: OkHttpClient = OkHttpUtils.create(),
    val baseUrl: String = "",
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
        queryParams: Map<String, String?> = emptyMap()
    ): Response {
        val fullUrl = OkHttpUtils.createFullUrl(baseUrl, url)
        return okHttpClient.get(fullUrl, queryParams)
    }

    suspend fun delete(
        url: String,
        queryParams: Map<String, String?> = emptyMap()
    ): Response {
        val fullUrl = OkHttpUtils.createFullUrl(baseUrl, url)
        return okHttpClient.delete(fullUrl, queryParams)
    }

    suspend fun post(
        url: String,
        json: String = EMPTY_JSON,
    ): Response {
        val fullUrl = OkHttpUtils.createFullUrl(baseUrl, url)
        return okHttpClient.post(fullUrl, json)
    }

    suspend inline fun <reified T : Any> post(
        url: String,
        body: T,
    ): Response {
        val fullUrl = OkHttpUtils.createFullUrl(baseUrl, url)
        return okHttpClient.post(fullUrl, body)
    }

    suspend fun <T : Any> post(
        url: String,
        kType: KType,
        body: T,
    ): Response {
        val fullUrl = OkHttpUtils.createFullUrl(baseUrl, url)
        return okHttpClient.post(fullUrl, kType, body)
    }

    suspend fun post(
        url: String,
        formData: Map<String, String>,
    ): Response {
        val fullUrl = OkHttpUtils.createFullUrl(baseUrl, url)
        return okHttpClient.post(fullUrl, formData)
    }

    suspend fun put(
        url: String,
        json: String = EMPTY_JSON,
    ): Response {
        val fullUrl = OkHttpUtils.createFullUrl(baseUrl, url)
        return okHttpClient.put(fullUrl, json)
    }

    suspend inline fun <reified T : Any> put(
        url: String,
        body: T,
    ): Response {
        val fullUrl = OkHttpUtils.createFullUrl(baseUrl, url)
        return okHttpClient.put(fullUrl, body)
    }

    suspend fun <T : Any> put(
        url: String,
        kType: KType,
        body: T,
    ): Response {
        val fullUrl = OkHttpUtils.createFullUrl(baseUrl, url)
        return okHttpClient.put(fullUrl, kType, body)
    }

    suspend fun put(
        url: String,
        formData: Map<String, String>,
    ): Response {
        val fullUrl = OkHttpUtils.createFullUrl(baseUrl, url)
        return okHttpClient.put(fullUrl, formData)
    }
}
