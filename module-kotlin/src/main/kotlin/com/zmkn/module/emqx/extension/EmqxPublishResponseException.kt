package com.zmkn.module.emqx.extension

import com.zmkn.module.emqx.exception.EmqxPublishResponseException
import com.zmkn.module.emqx.model.PublishExceptionResponseBody

fun EmqxPublishResponseException.toPublishExceptionResponseBody(): PublishExceptionResponseBody = PublishExceptionResponseBody(reasonCode, message)
