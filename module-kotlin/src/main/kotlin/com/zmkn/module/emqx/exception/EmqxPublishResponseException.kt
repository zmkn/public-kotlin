package com.zmkn.module.emqx.exception

import com.zmkn.module.okhttp.exception.OkHttpResponseException

open class EmqxPublishResponseException(
    val reasonCode: Int,
    override val message: String
) : OkHttpResponseException(message)
