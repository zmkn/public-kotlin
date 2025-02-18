package com.zmkn.extension

import com.google.protobuf.Value

@Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
fun <T> Value.toAny(): T {
    return when {
        hasBoolValue() -> boolValue
        hasNumberValue() -> numberValue
        hasStringValue() -> stringValue
        hasListValue() -> listValue.toList<Any?>()
        hasStructValue() -> structValue.toMap<Any?>()
        hasNullValue() -> null
        else -> null
    } as T
}
