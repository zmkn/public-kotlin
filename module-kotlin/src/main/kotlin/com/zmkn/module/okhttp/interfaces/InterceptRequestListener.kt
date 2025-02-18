package com.zmkn.module.okhttp.interfaces

import okhttp3.Interceptor
import okhttp3.Request

fun interface InterceptRequestListener {
    fun handler(
        chain: Interceptor.Chain,
        request: Request,
    ): Request
}
