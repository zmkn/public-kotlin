package com.zmkn.extension

import com.google.protobuf.Any as GoogleProtobufAny

fun Iterable<*>.toGoogleProtobufAnyIterable(): Iterable<Any> {
    return map {
        when (it) {
            is Array<*> -> it.toGoogleProtobufAnyArray()
            is Iterable<*> -> it.toGoogleProtobufAnyIterable()
            is Sequence<*> -> it.toGoogleProtobufAnySequence()
            is Map<*, *> -> it.toGoogleProtobufAnyMap()
            else -> it.toGoogleProtobufAny()
        }
    }
}

fun Iterable<*>.toAnyIterable(): Iterable<Any?> {
    return map {
        when (it) {
            is Array<*> -> it.toAnyArray()
            is Iterable<*> -> it.toAnyIterable()
            is Sequence<*> -> it.toAnySequence()
            is Map<*, *> -> it.toAnyMap()
            is GoogleProtobufAny -> it.toAny()
            else -> it
        }
    }
}
