package com.zmkn.module.kmongo.extension

import com.mongodb.client.result.InsertOneResult

fun InsertOneResult.isSuccessful(): Boolean = wasAcknowledged() && insertedId != null
