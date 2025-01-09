package com.zmkn.extension

fun Array<*>.toGoogleProtobufAnyArray(): Array<Any> {
    return toList().toGoogleProtobufAnyIterable().toList().toTypedArray()
}

fun Array<*>.toAnyArray(): Array<Any?> {
    return toList().toAnyIterable().toList().toTypedArray()
}
