package com.zmkn.module.kmongo.extension

import com.mongodb.client.result.InsertManyResult

fun InsertManyResult.isSuccessful(): Boolean = wasAcknowledged() && insertedIds.isNotEmpty()
