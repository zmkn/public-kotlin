package com.zmkn.module.kmongo.extension

import com.mongodb.client.result.DeleteResult

fun DeleteResult.isSuccessful(): Boolean = wasAcknowledged() && deletedCount > 0
