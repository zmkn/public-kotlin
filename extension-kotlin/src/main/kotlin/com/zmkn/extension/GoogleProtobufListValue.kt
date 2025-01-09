package com.zmkn.extension

import com.google.protobuf.ListValue

fun ListValue.toList(): List<Any?> = valuesList.map {
    it.toAny()
}
