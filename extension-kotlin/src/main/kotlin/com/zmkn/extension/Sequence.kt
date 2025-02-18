package com.zmkn.extension

fun <T> Sequence<*>.toProtobufAnySequence(): Sequence<T> {
    return toList().toProtobufAnyIterable<T>().asSequence()
}

fun <T> Sequence<*>.toAnySequence(): Sequence<T> {
    return toList().toAnyIterable<T>().asSequence()
}
