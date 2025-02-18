package com.zmkn.enumeration

enum class AuthorizationScheme(val scheme: String) {
    BASIC("Basic"),
    BEARER("Bearer");

    override fun toString(): String {
        return scheme
    }
}
