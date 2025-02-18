package com.zmkn.extension

import com.google.protobuf.Timestamp
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
fun Timestamp.toInstant(): Instant {
    return Instant.fromEpochSeconds(seconds, nanos)
}
