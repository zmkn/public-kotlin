package com.zmkn.extension

import com.google.protobuf.ListValue

fun <T> ListValue.toList(): List<T> = valuesList.map {
    it.toAny()
}
