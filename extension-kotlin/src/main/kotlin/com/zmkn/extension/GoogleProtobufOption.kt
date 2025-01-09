package com.zmkn.extension

import com.google.protobuf.Option

fun Option.toMap(): Map<String, Any?> = mapOf("name" to name, "value" to value.toAny())
