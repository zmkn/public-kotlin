package com.zmkn.extension

import com.google.protobuf.Struct

fun <V> Struct.toMap(): Map<String, V> = fieldsMap.mapValues { it.value.toAny() }
