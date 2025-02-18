package com.zmkn.util

object AuthorizationUtils {
    fun hasScheme(authorization: String, scheme: String): Boolean {
        return authorization.startsWith("$scheme ")
    }

    fun addScheme(token: String, scheme: String): String {
        return "$scheme $token"
    }

    fun removeScheme(authorization: String, scheme: String): String {
        return authorization.replace("$scheme ", "")
    }
}
