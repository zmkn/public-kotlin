package com.zmkn.module.okhttp.listener

import com.zmkn.module.okhttp.interfaces.InterceptRequestListener
import com.zmkn.module.okhttp.util.OkHttpUtils
import okhttp3.Interceptor
import okhttp3.Request

class BaseUrlInterceptRequestListener(
    private val baseUrl: String,
) : InterceptRequestListener {
    override fun handler(chain: Interceptor.Chain, request: Request): Request {
        val originalUrl = request.url.toString()
        return if (OkHttpUtils.isFullUrl(originalUrl)) {
            request
        } else {
            request.newBuilder().url(OkHttpUtils.createFullUrl(baseUrl, originalUrl)).build()
        }
    }
}
