package com.zmkn.module.okhttp.listener

import com.zmkn.enumeration.AuthorizationScheme
import com.zmkn.module.okhttp.interfaces.InterceptRequestListener
import com.zmkn.util.AuthorizationUtils
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request

class BearerAuthInterceptRequestListener(
    private val fetchToken: suspend () -> String
) : InterceptRequestListener {
    constructor(token: String) : this(suspend { token })
    constructor(fetchToken: () -> String) : this(suspend { fetchToken() })

    override fun handler(chain: Interceptor.Chain, request: Request): Request {
        val authorization = request.header("Authorization")
        return if (authorization == null) {
            val token = runBlocking {
                fetchToken()
            }
            val authorizationHeader = AuthorizationUtils.addScheme(token, AuthorizationScheme.BEARER.scheme)
            val newRequest: Request = request.newBuilder()
                .addHeader("Authorization", authorizationHeader)
                .build()
            newRequest
        } else {
            request
        }
    }
}
