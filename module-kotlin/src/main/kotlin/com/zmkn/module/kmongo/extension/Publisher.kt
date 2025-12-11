package com.zmkn.module.kmongo.extension

import com.zmkn.module.kmongo.util.KMongoUtils.documentToJson
import com.zmkn.module.kmongo.util.KMongoUtils.encodeToString
import com.zmkn.module.kmongo.util.KMongoUtils.jsonMapper
import org.bson.Document
import org.litote.kmongo.coroutine.toList
import org.reactivestreams.Publisher
import kotlin.reflect.KClass

suspend fun <T : Any> Publisher<T>.toJson(): String = jsonMapper.writeValueAsString(toList())

suspend fun <T : Any> Publisher<T>.toStringList(): List<String> = toList().map {
    jsonMapper.writeValueAsString(it)
}

suspend fun <T : Any> Publisher<T>.toStringList(schema: KClass<T>): List<String> = toList().map {
    if (it is Document) {
        documentToJson(it)
    } else {
        encodeToString(schema, it)
    }
}
