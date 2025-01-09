package com.zmkn.extension

fun Sequence<*>.toGoogleProtobufAnySequence(): Sequence<Any> {
    return toList().toGoogleProtobufAnyIterable().asSequence()
}

fun Sequence<*>.toAnySequence(): Sequence<Any?> {
    return toList().toAnyIterable().asSequence()
}
