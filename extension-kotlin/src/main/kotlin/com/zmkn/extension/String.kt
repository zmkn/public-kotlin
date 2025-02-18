package com.zmkn.extension

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

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

fun String.toDuration(): Duration {
    val s = trim()
    return if (s.isBlank()) {
        throw IllegalArgumentException("Duration string must not be blank.")
    } else {
        val pattern = "^\\d+[.\\d]+(ns|us|ms|s|m|h|d)$"
        val regex = Regex(pattern)
        val matchResult = regex.find(s)
        if (matchResult == null) {
            throw IllegalArgumentException("Not a valid Duration string format.")
        } else {
            val unitChar = matchResult.groupValues[1]
            val unit = when (unitChar) {
                "ns" -> DurationUnit.NANOSECONDS
                "us" -> DurationUnit.MICROSECONDS
                "ms" -> DurationUnit.MILLISECONDS
                "s" -> DurationUnit.SECONDS
                "m" -> DurationUnit.MINUTES
                "h" -> DurationUnit.HOURS
                "d" -> DurationUnit.DAYS
                else -> throw IllegalArgumentException("Unsupported duration unit character: $unitChar")
            }
            s.dropLast(unitChar.length).toDouble().toDuration(unit)
        }
    }
}
