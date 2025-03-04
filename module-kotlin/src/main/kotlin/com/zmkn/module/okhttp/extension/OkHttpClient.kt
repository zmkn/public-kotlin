package com.zmkn.module.okhttp.extension

import com.zmkn.module.okhttp.exception.OkHttpException
import com.zmkn.module.okhttp.util.OkHttpUtils
import com.zmkn.module.okhttp.util.OkHttpUtils.EMPTY_JSON
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import java.io.IOException
import kotlin.coroutines.resumeWithException
import kotlin.reflect.KType

suspend fun OkHttpClient.request(request: Request): Response {
    return suspendCancellableCoroutine { continuation ->
        val call = newCall(request)
        continuation.invokeOnCancellation {
            call.cancel()
        }
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val cause = e.cause
                val message = e.message
                val okHttpException = if (cause != null && message != null) {
                    OkHttpException(message, cause)
                } else if (message != null) {
                    OkHttpException(message)
                } else if (cause != null) {
                    OkHttpException(cause)
                } else {
                    OkHttpException()
                }
                continuation.resumeWithException(okHttpException)
            }

            override fun onResponse(call: Call, response: Response) {
                continuation.resumeWith(Result.success(response))
            }
        })
    }
}

suspend fun OkHttpClient.get(
    url: String,
    queryParams: Map<String, String?> = emptyMap(),
    headers: Headers? = null,
): Response {
    val newUrl = OkHttpUtils.createQueryHttpUrl(url, queryParams)
    val okHttpClientRequest = Request.Builder()
        .url(newUrl)
        .apply {
            headers?.also {
                setHeaders(it)
            }
        }
        .build()
    return request(okHttpClientRequest)
}

suspend fun OkHttpClient.delete(
    url: String,
    queryParams: Map<String, String?> = emptyMap(),
    headers: Headers? = null,
): Response {
    val newUrl = OkHttpUtils.createQueryHttpUrl(url, queryParams)
    val okHttpClientRequest = Request.Builder()
        .url(newUrl)
        .delete()
        .apply {
            headers?.also {
                setHeaders(it)
            }
        }
        .build()
    return request(okHttpClientRequest)
}

suspend fun OkHttpClient.post(
    url: String,
    json: String = EMPTY_JSON,
    headers: Headers? = null,
): Response {
    val requestBody = OkHttpUtils.createJsonRequestBody(json)
    val okHttpClientRequest = Request.Builder()
        .url(url)
        .post(requestBody)
        .apply {
            headers?.also {
                setHeaders(it)
            }
        }
        .build()
    return request(okHttpClientRequest)
}

suspend inline fun <reified T : Any> OkHttpClient.post(
    url: String,
    body: T,
    headers: Headers? = null,
): Response {
    val json = OkHttpUtils.encodeToString(body)
    return post(url, json, headers)
}

suspend fun <T : Any> OkHttpClient.post(
    url: String,
    kType: KType,
    body: T,
    headers: Headers? = null,
): Response {
    val json = OkHttpUtils.encodeToString(kType, body)
    return post(url, json, headers)
}

suspend fun OkHttpClient.post(
    url: String,
    formData: Map<String, String>,
    headers: Headers? = null,
): Response {
    val requestBody = OkHttpUtils.createFormRequestBody(formData)
    val okHttpClientRequest = Request.Builder()
        .url(url)
        .post(requestBody)
        .apply {
            headers?.also {
                setHeaders(it)
            }
        }
        .build()
    return request(okHttpClientRequest)
}

suspend fun OkHttpClient.put(
    url: String,
    json: String = EMPTY_JSON,
    headers: Headers? = null,
): Response {
    val requestBody = OkHttpUtils.createJsonRequestBody(json)
    val okHttpClientRequest = Request.Builder()
        .url(url)
        .put(requestBody)
        .apply {
            headers?.also {
                setHeaders(it)
            }
        }
        .build()
    return request(okHttpClientRequest)
}

suspend inline fun <reified T : Any> OkHttpClient.put(
    url: String,
    body: T,
    headers: Headers? = null,
): Response {
    val json = OkHttpUtils.encodeToString(body)
    return put(url, json, headers)
}

suspend fun <T : Any> OkHttpClient.put(
    url: String,
    kType: KType,
    body: T,
    headers: Headers? = null,
): Response {
    val json = OkHttpUtils.encodeToString(kType, body)
    return put(url, json, headers)
}

suspend fun OkHttpClient.put(
    url: String,
    formData: Map<String, String>,
    headers: Headers? = null,
): Response {
    val requestBody = OkHttpUtils.createFormRequestBody(formData)
    val okHttpClientRequest = Request.Builder()
        .url(url)
        .put(requestBody)
        .apply {
            headers?.also {
                setHeaders(it)
            }
        }
        .build()
    return request(okHttpClientRequest)
}
