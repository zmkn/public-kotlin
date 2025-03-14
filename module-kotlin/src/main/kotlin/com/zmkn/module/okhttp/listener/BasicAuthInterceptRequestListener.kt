package com.zmkn.module.okhttp.listener

import com.zmkn.enumeration.AuthorizationScheme
import com.zmkn.module.okhttp.interfaces.InterceptRequestListener
import com.zmkn.util.AuthorizationUtils
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import java.util.*

class BasicAuthInterceptRequestListener(
    private val fetchCredentials: suspend () -> Pair<String, String>
) : InterceptRequestListener {
    constructor(username: String, password: String) : this(suspend { Pair(username, password) })
    constructor(fetchCredentials: () -> Pair<String, String>) : this(suspend { fetchCredentials() })

    override fun handler(chain: Interceptor.Chain, request: Request): Request {
        val authorization = request.header("Authorization")
        return if (authorization == null) {
            val credentials = runBlocking {
                fetchCredentials().run {
                    "$first:$second"
                }
            }
            val authorizationHeader = AuthorizationUtils.addScheme(Base64.getEncoder().encodeToString(credentials.toByteArray()), AuthorizationScheme.BASIC.scheme)
            val newRequest: Request = request.newBuilder()
                .addHeader("Authorization", authorizationHeader)
                .build()
            newRequest
        } else {
            request
        }
    }
}
