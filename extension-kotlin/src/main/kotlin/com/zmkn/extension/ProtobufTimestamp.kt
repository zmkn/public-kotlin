package com.zmkn.extension

import com.google.protobuf.Timestamp
import kotlin.time.Instant

fun Timestamp.toInstant(): Instant = Instant.fromEpochSeconds(seconds, nanos)
