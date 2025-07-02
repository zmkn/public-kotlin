package com.zmkn.module.kmongo.extension

import com.mongodb.bulk.BulkWriteResult

fun BulkWriteResult.isSuccessful(): Boolean = wasAcknowledged()
