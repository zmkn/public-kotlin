package com.zmkn.module.kmongo

import com.mongodb.reactivestreams.client.ClientSession
import com.zmkn.module.kmongo.util.KMongoUtils.getCollectionName
import com.zmkn.module.kmongo.util.KMongoUtils.registerCustomCodec
import org.bson.Document
import org.litote.kmongo.coroutine.*
import kotlin.reflect.KClass
import org.litote.kmongo.reactivestreams.KMongo as KMongoUtils

class KMongo(connectionString: String, databaseName: String) {
    private val _client: CoroutineClient = KMongoUtils.createClient(connectionString).coroutine
    private val _database: CoroutineDatabase = _client.getDatabase(databaseName)

    val database: CoroutineDatabase = _database

    fun <T : Any> getCollection(kClass: KClass<T>): CoroutineCollection<T> {
        val collectionName = getCollectionName(kClass)
        return getCollection(collectionName, kClass)
    }

    fun getCollection(collectionName: String): CoroutineCollection<Document> {
        return _database.database.getCollection(collectionName).coroutine
    }

    fun <T : Any> getCollection(collectionName: String, kClass: KClass<T>): CoroutineCollection<T> {
        return _database.database.getCollection(collectionName, kClass.java).coroutine
    }

    suspend fun <T : Any> collectionExists(kClass: KClass<T>): Boolean {
        val collectionName = getCollectionName(kClass)
        return collectionExists(collectionName)
    }

    suspend fun collectionExists(collectionName: String): Boolean {
        return collectionName in _database.listCollectionNames()
    }

    suspend fun <T> withTransaction(block: suspend (session: ClientSession) -> T?): T? {
        val session = _client.startSession()
        return try {
            session.startTransaction()
            session.hasActiveTransaction()
            val result = block(session)
            if (session.hasActiveTransaction()) {
                session.commitTransactionAndAwait()
            }
            result
        } catch (e: Exception) {
            if (session.hasActiveTransaction()) {
                session.abortTransactionAndAwait()
            }
            throw e
        } finally {
            session.close()
        }
    }

    fun close() {
        _client.client.close()
    }

    companion object {
        init {
            registerCustomCodec()
        }
    }
}
