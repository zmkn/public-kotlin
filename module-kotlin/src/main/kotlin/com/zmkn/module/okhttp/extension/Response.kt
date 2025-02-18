package com.zmkn.module.okhttp.extension

import okhttp3.Headers
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import kotlin.reflect.KClass

suspend fun Response.onSuccess(
    callback: suspend (
        request: Request,
        headers: Headers,
        body: ResponseBody?
    ) -> Unit
): Response {
    if (isSuccessful) {
        callback(request, headers, body)
    }
    return this
}

suspend fun Response.onFailure(
    callback: suspend (
        request: Request,
        headers: Headers,
        body: ResponseBody?
    ) -> Unit
): Response {
    if (!isSuccessful) {
        callback(request, headers, body)
    }
    return this
}

suspend fun Response.onComplete(
    onSuccess: suspend (
        request: Request,
        headers: Headers,
        body: ResponseBody?
    ) -> Unit,
    onFailure: suspend (
        request: Request,
        headers: Headers,
        body: ResponseBody?
    ) -> Unit,
): Response {
    if (isSuccessful) {
        onSuccess(request, headers, body)
    } else {
        onFailure(request, headers, body)
    }
    return this
}

inline fun <reified T : Any> Response.convertBody(): T? {
    return body?.convert<T>()
}

fun <T : Any> Response.convertBody(schema: KClass<T>): T? {
    return body?.convert(schema)
}

inline fun <reified T : Any> Response.convertBodyOnSuccess(): T? {
    return if (isSuccessful) {
        body?.convert<T>()
    } else {
        null
    }
}

fun <T : Any> Response.convertBodyOnSuccess(schema: KClass<T>): T? {
    return if (isSuccessful) {
        body?.convert(schema)
    } else {
        null
    }
}

inline fun <reified T : Any> Response.convertBodyOnFailure(): T? {
    return if (!isSuccessful) {
        body?.convert<T>()
    } else {
        null
    }
}

fun <T : Any> Response.convertBodyOnFailure(schema: KClass<T>): T? {
    return if (!isSuccessful) {
        body?.convert(schema)
    } else {
        null
    }
}
