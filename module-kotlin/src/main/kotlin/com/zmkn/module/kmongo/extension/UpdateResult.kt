package com.zmkn.module.kmongo.extension

import com.mongodb.client.result.UpdateResult

fun UpdateResult.isSuccessful(): Boolean = wasAcknowledged() && matchedCount > 0
