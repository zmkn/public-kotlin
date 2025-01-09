package com.zmkn.extension

import com.google.protobuf.Any as GoogleProtobufAny

fun Map<*, *>.toGoogleProtobufAnyMap(): Map<Any?, Any> {
    return mapValues { (_, value) ->
        when (value) {
            is Array<*> -> value.toGoogleProtobufAnyArray()
            is Iterable<*> -> value.toGoogleProtobufAnyIterable()
            is Sequence<*> -> value.toGoogleProtobufAnySequence()
            is Map<*, *> -> value.toGoogleProtobufAnyMap()
            else -> value.toGoogleProtobufAny()
        }
    }
}

fun Map<*, *>.toAnyMap(): Map<Any?, Any?> {
    return mapValues { (_, value) ->
        when (value) {
            is Array<*> -> value.toAnyArray()
            is Iterable<*> -> value.toAnyIterable()
            is Sequence<*> -> value.toAnySequence()
            is Map<*, *> -> value.toAnyMap()
            is GoogleProtobufAny -> value.toAny()
            else -> value
        }
    }
}
