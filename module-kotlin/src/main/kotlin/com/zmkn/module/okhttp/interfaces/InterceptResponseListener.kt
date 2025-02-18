package com.zmkn.module.okhttp.interfaces

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

fun interface InterceptResponseListener {
    fun handler(
        chain: Interceptor.Chain,
        request: Request,
        response: Response?,
    ): Response
}
