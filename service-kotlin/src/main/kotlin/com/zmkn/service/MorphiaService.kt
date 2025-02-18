package com.zmkn.service

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import dev.morphia.Datastore
import dev.morphia.Morphia
import dev.morphia.config.MorphiaConfig
import kotlinx.coroutines.*

class MorphiaService {
    constructor(
        connectionString: String,
        databaseName: String,
    ) : this(
        getDefaultConfig().database(databaseName),
        connectionString,
    )

    constructor(
        connectionString: String,
        databaseName: String,
        packages: List<String>,
    ) : this(
        getDefaultConfig().database(databaseName).packages(packages),
        connectionString,
    )

    constructor(
        morphiaConfig: MorphiaConfig,
        connectionString: String
    ) {
        _mongoClient = MongoClients.create(connectionString)
        _datastore = Morphia.createDatastore(_mongoClient, morphiaConfig)
    }

    private val _datastore: Datastore
    private val _mongoClient: MongoClient
    private val _supervisorScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val datastore get() = _datastore
    val mongoClient get() = _mongoClient

    fun <T> async(block: suspend (coroutineScope: CoroutineScope) -> T): Deferred<T> {
        return _supervisorScope.async {
            block(this)
        }
    }

    fun launch(block: suspend (coroutineScope: CoroutineScope) -> Unit): Job {
        return _supervisorScope.launch {
            block(this)
        }
    }

    fun close() {
        _supervisorScope.cancel()
        _mongoClient.close()
    }

    companion object {
        const val CONFIG_RESOURCE_PATH = "morphia.properties"

        fun getDefaultConfig(): MorphiaConfig {
            return MorphiaConfig.load(CONFIG_RESOURCE_PATH)
        }
    }
}
