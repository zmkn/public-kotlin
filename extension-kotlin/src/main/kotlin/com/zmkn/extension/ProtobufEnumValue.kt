package com.zmkn.extension

import com.google.protobuf.EnumValue

fun EnumValue.toList(): List<Map<String, Any?>> = optionsList.map {
    it.toMap()
}
