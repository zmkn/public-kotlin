package com.zmkn.extension

import com.google.protobuf.Any as ProtobufAny

@Suppress("UNCHECKED_CAST")
fun <T> Iterable<*>.toProtobufAnyIterable(): Iterable<T> {
    return map {
        when (it) {
            is Array<*> -> it.toProtobufAnyArray<Any?>()
            is Iterable<*> -> it.toProtobufAnyIterable<Any?>()
            is Sequence<*> -> it.toProtobufAnySequence<Any?>()
            is Map<*, *> -> it.toProtobufAnyMap<Any?, Any?>()
            else -> it.toProtobufAny()
        }
    } as List<T>
}

@Suppress("UNCHECKED_CAST")
fun <T> Iterable<*>.toAnyIterable(): Iterable<T> {
    return map {
        when (it) {
            is Array<*> -> it.toAnyArray<Any?>()
            is Iterable<*> -> it.toAnyIterable<Any?>()
            is Sequence<*> -> it.toAnySequence<Any?>()
            is Map<*, *> -> it.toAnyMap<Any?, Any?>()
            is ProtobufAny -> it.toAny()
            else -> it
        }
    } as List<T>
}
