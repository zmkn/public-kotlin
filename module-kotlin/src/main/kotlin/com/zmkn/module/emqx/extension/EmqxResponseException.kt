package com.zmkn.module.emqx.extension

import com.zmkn.module.emqx.exception.EmqxResponseException
import com.zmkn.module.emqx.model.ExceptionResponseBody

fun EmqxResponseException.toExceptionResponseBody(): ExceptionResponseBody = ExceptionResponseBody(code, message)
