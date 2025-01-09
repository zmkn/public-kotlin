package com.zmkn.extension

import com.google.protobuf.Value

fun Value.toAny(): Any? {
    return when {
        hasBoolValue() -> boolValue
        hasNumberValue() -> numberValue
        hasStringValue() -> stringValue
        hasListValue() -> listValue.toList()
        hasStructValue() -> structValue.toMap()
        hasNullValue() -> null
        else -> null
    }
}
