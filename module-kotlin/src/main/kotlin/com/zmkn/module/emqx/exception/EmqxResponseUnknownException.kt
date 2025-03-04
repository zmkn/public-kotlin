package com.zmkn.module.emqx.exception

import com.zmkn.module.okhttp.exception.OkHttpResponseException

open class EmqxResponseUnknownException : OkHttpResponseException {
    constructor() : super()
    constructor(message: String) : super(message)
}
