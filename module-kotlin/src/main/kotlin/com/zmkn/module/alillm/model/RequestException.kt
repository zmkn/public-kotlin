package com.zmkn.module.alillm.model

import com.alibaba.dashscope.exception.ApiException
import com.zmkn.module.alillm.enumeration.ResponseCode

class RequestException(e: Throwable) : RuntimeException(e) {
    val responseCode: ResponseCode =
        if (e is ApiException) {
            val status = e.status
            ResponseCode.fromCodeAndStatusCode(status.code, status.statusCode)
        } else {
            ResponseCode.UNKNOWN_ERROR
        }

    override val cause: Throwable = e
    override val message: String = responseCode.toString()
}
