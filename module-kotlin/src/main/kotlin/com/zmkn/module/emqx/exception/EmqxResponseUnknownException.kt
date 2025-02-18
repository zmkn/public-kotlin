package com.zmkn.module.emqx.exception

import com.zmkn.module.okhttp.exception.OkHttpResponseException

open class EmqxResponseUnknownException(
    override val message: String
) : OkHttpResponseException(message)
