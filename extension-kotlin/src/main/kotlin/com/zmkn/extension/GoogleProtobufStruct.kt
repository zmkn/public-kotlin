package com.zmkn.extension

import com.google.protobuf.Struct

fun Struct.toMap(): Map<String, Any?> = fieldsMap.mapValues { it.value.toAny() }
