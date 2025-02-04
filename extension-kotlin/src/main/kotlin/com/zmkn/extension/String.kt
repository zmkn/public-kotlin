package com.zmkn.extension

fun String.toNumberOrNull(): Number? {
    return when {
        contains(".") -> {
            if (contains("f", ignoreCase = true)) {
                toFloatOrNull()
            } else {
                toDoubleOrNull()
            }
        }

        else -> toLongOrNull() ?: toIntOrNull()
    }
}
