package com.zmkn.enumeration

enum class AuthorizationScheme(val scheme: String) {
    BASIC("Basic"),
    BEARER("Bearer");

    override fun toString(): String {
        return scheme
    }

    companion object {
        fun fromScheme(scheme: String): AuthorizationScheme? {
            AuthorizationScheme.entries.forEach {
                if (scheme == it.scheme) {
                    return it
                }
            }
            return null
        }
    }
}
