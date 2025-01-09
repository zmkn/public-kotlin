package com.zmkn.module.kmongo.extension

import com.zmkn.module.kmongo.util.KMongoUtils.encodeToString
import com.zmkn.module.kmongo.util.KMongoUtils.objectMapper
import org.bson.Document
import org.litote.kmongo.coroutine.toList
import org.reactivestreams.Publisher
import kotlin.reflect.KClass

suspend fun <T : Any> Publisher<T>.toJson(): String {
    return objectMapper.writeValueAsString(toList())
}

suspend inline fun <reified T : Any> Publisher<T>.toStringList(): List<String> {
    return toStringList(T::class)
}

suspend fun <T : Any> Publisher<T>.toStringList(schema: KClass<T>): List<String> {
    return toList().map {
        if (it is Document) {
            it.toJson()
        } else {
            encodeToString(schema, it)
        }
    }
}
