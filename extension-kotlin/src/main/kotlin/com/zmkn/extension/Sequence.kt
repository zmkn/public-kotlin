package com.zmkn.extension

fun <T> Sequence<*>.toProtobufAnySequence(): Sequence<T> = toList().toProtobufAnyIterable<T>().asSequence()

fun <T> Sequence<*>.toAnySequence(): Sequence<T> = toList().toAnyIterable<T>().asSequence()
