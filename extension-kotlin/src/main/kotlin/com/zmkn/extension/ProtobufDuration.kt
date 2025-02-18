package com.zmkn.extension

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration
import com.google.protobuf.Duration as ProtobufDuration

fun ProtobufDuration.toDuration(): Duration {
    val s = seconds.toDouble() + nanos.toDouble() / 1000000000
    return s.toDuration(DurationUnit.SECONDS)
}
