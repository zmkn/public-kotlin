package com.zmkn.module.okhttp.extension

import com.zmkn.module.okhttp.util.OkHttpUtils
import okhttp3.ResponseBody
import kotlin.reflect.KClass

inline fun <reified T : Any> ResponseBody.convert(): T {
    return OkHttpUtils.decodeFromString<T>(string())
}

fun <T : Any> ResponseBody.convert(schema: KClass<T>): T {
    return OkHttpUtils.decodeFromString(schema, string())
}
