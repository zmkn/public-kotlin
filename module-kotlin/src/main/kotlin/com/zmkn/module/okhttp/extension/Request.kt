package com.zmkn.module.okhttp.extension

import okhttp3.Headers
import okhttp3.Request

fun Request.Builder.setHeaders(headers: Headers): Request.Builder {
    headers.forEach {
        header(it.first, it.second)
    }
    return this
}

fun Request.Builder.replaceHeaders(headers: Headers): Request.Builder {
    this.headers(headers)
    return this
}

fun Request.Builder.mergeHeaders(headers: Headers): Request.Builder {
    headers.forEach {
        addHeader(it.first, it.second)
    }
    return this
}
