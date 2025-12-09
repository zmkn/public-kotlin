package com.zmkn.extension

inline fun <reified T> Array<*>.toProtobufAnyArray(): Array<T> = toList().toProtobufAnyIterable<T>().toList().toTypedArray()

inline fun <reified T> Array<*>.toAnyArray(): Array<T> = toList().toAnyIterable<T>().toList().toTypedArray()
