package com.zmkn.module.kmongo

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.event.ConnectionCheckedInEvent
import com.mongodb.event.ConnectionCheckedOutEvent
import com.mongodb.reactivestreams.client.ClientSession
import com.zmkn.module.kmongo.util.KMongoUtils.getCollectionName
import com.zmkn.module.kmongo.util.KMongoUtils.registerCustomCodec
import org.bson.Document
import org.litote.kmongo.coroutine.*
import java.io.Closeable
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.reflect.KClass
import com.mongodb.event.ConnectionPoolListener as MongodbConnectionPoolListener
import org.litote.kmongo.reactivestreams.KMongo as KMongoUtils

class KMongo(connectionString: String, databaseName: String) : Closeable {
    private var _isOpened: Boolean = false
    private val _lock = ReentrantLock()
    private val _connectionListener = ConnectionPoolListener()
    private val _client: CoroutineClient by lazy {
        _lock.withLock {
            KMongoUtils.createClient(
                MongoClientSettings
                    .builder()
                    .applyConnectionString(ConnectionString(connectionString))
                    .applyToConnectionPoolSettings { builder ->
                        builder.addConnectionPoolListener(_connectionListener)
                    }
                    .build()
            ).coroutine.apply {
                _isOpened = true
            }
        }
    }
    private val _database: CoroutineDatabase by lazy {
        _client.getDatabase(databaseName)
    }

    val isOpened: Boolean
        get() = _isOpened
    val database: CoroutineDatabase
        get() = _database
    val activeConnections: Int
        get() = _connectionListener.activeConnections

    class ConnectionPoolListener : MongodbConnectionPoolListener {
        var activeConnections: Int = 0

        override fun connectionCheckedOut(event: ConnectionCheckedOutEvent?) {
            activeConnections += 1
        }

        override fun connectionCheckedIn(event: ConnectionCheckedInEvent?) {
            activeConnections -= 1
        }
    }

    fun <T : Any> getCollection(collectionName: String, collectionTypeKClass: KClass<T>): CoroutineCollection<T> = _database.database.getCollection(collectionName, collectionTypeKClass.java).coroutine

    fun <T : Any> getCollection(collectionNameKClass: KClass<*>, collectionTypeKClass: KClass<T>): CoroutineCollection<T> {
        val collectionName = getCollectionName(collectionNameKClass)
        return getCollection(collectionName, collectionTypeKClass)
    }

    fun getCollection(collectionName: String): CoroutineCollection<Document> = _database.database.getCollection(collectionName).coroutine

    fun getCollection(collectionNameKClass: KClass<*>): CoroutineCollection<Document> {
        val collectionName = getCollectionName(collectionNameKClass)
        return getCollection(collectionName)
    }

    suspend fun <T : Any> collectionExists(kClass: KClass<T>): Boolean {
        val collectionName = getCollectionName(kClass)
        return collectionExists(collectionName)
    }

    suspend fun collectionExists(collectionName: String): Boolean = collectionName in _database.listCollectionNames()

    suspend fun <T> withTransaction(block: suspend (session: ClientSession) -> T): T {
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

    fun hasActiveConnections(): Boolean = activeConnections > 0

    override fun close() {
        _lock.withLock {
            _client.close()
            _isOpened = false
        }
    }

    companion object {
        init {
            registerCustomCodec()
        }
    }
}
