package com.zmkn.extension

inline fun <reified T> Array<*>.toProtobufAnyArray(): Array<T> {
    return toList().toProtobufAnyIterable<T>().toList().toTypedArray()
}

inline fun <reified T> Array<*>.toAnyArray(): Array<T> {
    return toList().toAnyIterable<T>().toList().toTypedArray()
}
