package com.zmkn.util

object AuthorizationUtils {
    fun hasScheme(authorization: String, scheme: String): Boolean = authorization.startsWith("$scheme ")

    fun addScheme(token: String, scheme: String): String = "$scheme $token"

    fun removeScheme(authorization: String, scheme: String): String = authorization.replace("$scheme ", "")
}
