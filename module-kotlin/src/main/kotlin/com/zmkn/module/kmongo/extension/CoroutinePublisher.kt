package com.zmkn.module.kmongo.extension

import com.mongodb.MongoNamespace
import com.mongodb.bulk.BulkWriteResult
import com.mongodb.client.model.*
import com.mongodb.client.result.DeleteResult
import com.mongodb.client.result.InsertManyResult
import com.mongodb.client.result.InsertOneResult
import com.mongodb.client.result.UpdateResult
import com.mongodb.reactivestreams.client.ClientSession
import com.zmkn.module.kmongo.model.MultipleProjection
import com.zmkn.module.kmongo.util.KMongoUtils.EMPTY_JSON
import com.zmkn.module.kmongo.util.KMongoUtils.decodeFromString
import com.zmkn.module.kmongo.util.KMongoUtils.documentToJson
import com.zmkn.module.kmongo.util.KMongoUtils.encodeToString
import com.zmkn.module.kmongo.util.KMongoUtils.jsonToBson
import com.zmkn.module.kmongo.util.KMongoUtils.jsonToDocument
import com.zmkn.module.kmongo.util.KMongoUtils.objectMapper
import com.zmkn.module.kmongo.util.multipleProjectionCodecRegistry
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.bson.BsonDocument
import org.bson.Document
import org.litote.kmongo.coroutine.*
import org.litote.kmongo.path
import org.litote.kmongo.reactivestreams.map
import org.litote.kmongo.util.KMongoUtil
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

suspend fun <T : Any> CoroutinePublisher<T>.toJson(): String {
    return publisher.toJson()
}

suspend inline fun <reified T : Any> CoroutinePublisher<T>.toStringList(): List<String> {
    return toStringList(T::class)
}

suspend fun <T : Any> CoroutinePublisher<T>.toStringList(schema: KClass<T>): List<String> {
    return publisher.toStringList(schema)
}

suspend fun <T : Any> CoroutineCollection<T>.dropCollection(clientSession: ClientSession? = null): Void? {
    return if (clientSession == null) {
        collection.drop()
    } else {
        collection.drop(clientSession)
    }.awaitFirstOrNull()
}

suspend fun <T : Any> CoroutineCollection<T>.renameCollection(newCollectionNamespace: MongoNamespace, options: RenameCollectionOptions = RenameCollectionOptions(), clientSession: ClientSession? = null): Void? {
    return if (clientSession == null) {
        collection.renameCollection(newCollectionNamespace, options)
    } else {
        collection.renameCollection(clientSession, newCollectionNamespace, options)
    }.awaitFirstOrNull()
}

suspend fun <T : Any> CoroutineCollection<T>.dropIndex(indexName: String, dropIndexOptions: DropIndexOptions = DropIndexOptions(), clientSession: ClientSession? = null): Void? {
    return if (clientSession == null) {
        collection.dropIndex(indexName, dropIndexOptions)
    } else {
        collection.dropIndex(clientSession, indexName, dropIndexOptions)
    }.awaitFirstOrNull()
}

fun <T : Any> CoroutineCollection<T>.listIndexes(clientSession: ClientSession? = null): CoroutineListIndexesPublisher<Document> {
    return if (clientSession == null) {
        collection.listIndexes().coroutine
    } else {
        collection.listIndexes(clientSession).coroutine
    }
}

inline fun <reified T : Any> CoroutineCollection<T>.distinctByField(fieldName: String, filter: String = EMPTY_JSON, clientSession: ClientSession? = null): CoroutineDistinctPublisher<out Any> {
    return distinctByField(T::class, fieldName, filter, clientSession)
}

fun <T : Any> CoroutineCollection<T>.distinctByField(schema: KClass<T>, fieldName: String, filter: String = EMPTY_JSON, clientSession: ClientSession? = null): CoroutineDistinctPublisher<out Any> {
    return schema.memberProperties.find {
        it.name == fieldName
    }?.let {
        if (clientSession == null) {
            collection.distinct(it.path(), jsonToBson(filter), (it.returnType.classifier as KClass<*>).java).coroutine
        } else {
            collection.distinct(clientSession, it.path(), jsonToBson(filter), (it.returnType.classifier as KClass<*>).java).coroutine
        }
    } ?: throw IllegalArgumentException("The field name '$fieldName' does not match any property in the class ${schema.simpleName}.")
}

fun <T : Any> CoroutineCollection<T>.distinctByField(fieldName: String, fieldType: Class<*>, filter: String = EMPTY_JSON, clientSession: ClientSession? = null): CoroutineDistinctPublisher<out Any> {
    return if (clientSession == null) {
        collection.distinct(fieldName, jsonToBson(filter), fieldType).coroutine
    } else {
        collection.distinct(clientSession, fieldName, jsonToBson(filter), fieldType).coroutine
    }
}

fun <T : Any> CoroutineCollection<T>.find(filter: String = EMPTY_JSON, clientSession: ClientSession? = null): CoroutineFindPublisher<T> {
    return if (clientSession == null) {
        find(filter)
    } else {
        find(clientSession, jsonToBson(filter))
    }
}

suspend inline fun <reified T : Any> CoroutineCollection<T>.findOneAsString(filter: String = EMPTY_JSON, clientSession: ClientSession? = null): String? {
    return findOneAsString(T::class, filter, clientSession)
}

suspend fun <T : Any> CoroutineCollection<T>.findOneAsString(schema: KClass<T>, filter: String = EMPTY_JSON, clientSession: ClientSession? = null): String? {
    return if (clientSession == null) {
        findOne(filter)
    } else {
        findOne(clientSession, filter)
    }?.let {
        if (schema == Document::class) {
            documentToJson(it as Document)
        } else {
            encodeToString(schema, it)
        }
    }
}

inline fun <reified T : Any> CoroutineCollection<T>.aggregate(pipeline: List<String>, clientSession: ClientSession? = null): CoroutineAggregatePublisher<T> {
    return aggregate(T::class, pipeline, clientSession)
}

fun <T : Any> CoroutineCollection<T>.aggregate(schema: KClass<T>, pipeline: List<String>, clientSession: ClientSession? = null): CoroutineAggregatePublisher<T> {
    return if (schema == Document::class) {
        if (clientSession == null) {
            collection.aggregate(KMongoUtil.toBsonList(pipeline.toTypedArray(), codecRegistry)).coroutine
        } else {
            collection.aggregate(clientSession, KMongoUtil.toBsonList(pipeline.toTypedArray(), codecRegistry)).coroutine
        }
    } else {
        if (clientSession == null) {
            collection.aggregate(KMongoUtil.toBsonList(pipeline.toTypedArray(), codecRegistry), schema.java).coroutine
        } else {
            collection.aggregate(clientSession, KMongoUtil.toBsonList(pipeline.toTypedArray(), codecRegistry), schema.java).coroutine
        }
    }
}

suspend inline fun <reified T : Any> CoroutineCollection<T>.insertOne(documentString: String, options: InsertOneOptions = InsertOneOptions(), clientSession: ClientSession? = null): InsertOneResult {
    return insertOne(T::class, documentString, options, clientSession)
}

@Suppress("UNCHECKED_CAST")
suspend fun <T : Any> CoroutineCollection<T>.insertOne(schema: KClass<T>, documentString: String, options: InsertOneOptions = InsertOneOptions(), clientSession: ClientSession? = null): InsertOneResult {
    val document = if (schema == Document::class) {
        jsonToDocument(documentString) as T
    } else {
        decodeFromString(schema, documentString)
    }
    return if (clientSession == null) {
        insertOne(document, options)
    } else {
        insertOne(clientSession, document, options)
    }
}

suspend inline fun <reified T : Any> CoroutineCollection<T>.insertMany(documentsString: List<String>, options: InsertManyOptions = InsertManyOptions(), clientSession: ClientSession? = null): InsertManyResult {
    return insertMany(T::class, documentsString, options, clientSession)
}

@Suppress("UNCHECKED_CAST")
suspend fun <T : Any> CoroutineCollection<T>.insertMany(schema: KClass<T>, documentsString: List<String>, options: InsertManyOptions = InsertManyOptions(), clientSession: ClientSession? = null): InsertManyResult {
    val documents = if (schema == Document::class) {
        documentsString.map {
            jsonToDocument(it) as T
        }
    } else {
        documentsString.map {
            decodeFromString(schema, it)
        }
    }
    return if (clientSession == null) {
        insertMany(documents, options)
    } else {
        insertMany(clientSession, documents, options)
    }
}

suspend fun <T : Any> CoroutineCollection<T>.updateOne(filter: String, update: String, options: UpdateOptions = UpdateOptions(), clientSession: ClientSession? = null): UpdateResult {
    return if (clientSession == null) {
        updateOne(filter, update, options)
    } else {
        updateOne(clientSession, filter, update, options)
    }
}

suspend fun <T : Any> CoroutineCollection<T>.updateMany(filter: String, update: String, options: UpdateOptions = UpdateOptions(), clientSession: ClientSession? = null): UpdateResult {
    return if (clientSession == null) {
        updateMany(filter, update, options)
    } else {
        updateMany(clientSession, filter, update, options)
    }
}

suspend inline fun <reified T : Any> CoroutineCollection<T>.replaceOne(filter: String, replacement: String, options: ReplaceOptions = ReplaceOptions(), clientSession: ClientSession? = null): UpdateResult {
    return replaceOne(T::class, filter, replacement, options, clientSession)
}

@Suppress("UNCHECKED_CAST")
suspend fun <T : Any> CoroutineCollection<T>.replaceOne(schema: KClass<T>, filter: String, replacement: String, options: ReplaceOptions = ReplaceOptions(), clientSession: ClientSession? = null): UpdateResult {
    val replacementDocument = if (schema == Document::class) {
        jsonToDocument(replacement) as T
    } else {
        decodeFromString(schema, replacement)
    }
    return if (clientSession == null) {
        replaceOne(filter, replacementDocument, options)
    } else {
        replaceOne(clientSession, jsonToBson(filter), replacementDocument, options)
    }
}

suspend inline fun <reified T : Any> CoroutineCollection<T>.findOneAndUpdateAsString(filter: String, update: String, options: FindOneAndUpdateOptions = FindOneAndUpdateOptions(), clientSession: ClientSession? = null): String? {
    return findOneAndUpdateAsString(T::class, filter, update, options, clientSession)
}

suspend fun <T : Any> CoroutineCollection<T>.findOneAndUpdateAsString(schema: KClass<T>, filter: String, update: String, options: FindOneAndUpdateOptions = FindOneAndUpdateOptions(), clientSession: ClientSession? = null): String? {
    return if (clientSession == null) {
        findOneAndUpdate(filter, update, options)
    } else {
        findOneAndUpdate(clientSession, filter, update, options)
    }?.let {
        if (schema == Document::class) {
            documentToJson(it as Document)
        } else {
            encodeToString(schema, it)
        }
    }
}

suspend inline fun <reified T : Any> CoroutineCollection<T>.findOneAndReplaceAsString(filter: String, replacement: String, options: FindOneAndReplaceOptions = FindOneAndReplaceOptions(), clientSession: ClientSession? = null): String? {
    return findOneAndReplaceAsString(T::class, filter, replacement, options, clientSession)
}

suspend fun <T : Any> CoroutineCollection<T>.findOneAndReplaceAsString(schema: KClass<T>, filter: String, replacement: String, options: FindOneAndReplaceOptions = FindOneAndReplaceOptions(), clientSession: ClientSession? = null): String? {
    val replacementDocument = decodeFromString(schema, replacement)
    return if (clientSession == null) {
        findOneAndReplace(filter, replacementDocument, options)
    } else {
        findOneAndReplace(clientSession, filter, replacementDocument, options)
    }?.let {
        if (schema == Document::class) {
            documentToJson(it as Document)
        } else {
            encodeToString(schema, it)
        }
    }
}

suspend inline fun <reified T : Any> CoroutineCollection<T>.findOneAndDeleteAsString(filter: String, options: FindOneAndDeleteOptions = FindOneAndDeleteOptions(), clientSession: ClientSession? = null): String? {
    return findOneAndDeleteAsString(T::class, filter, options, clientSession)
}

suspend fun <T : Any> CoroutineCollection<T>.findOneAndDeleteAsString(schema: KClass<T>, filter: String, options: FindOneAndDeleteOptions = FindOneAndDeleteOptions(), clientSession: ClientSession? = null): String? {
    return if (clientSession == null) {
        findOneAndDelete(filter, options)
    } else {
        findOneAndDelete(clientSession, filter, options)
    }?.let {
        if (schema == Document::class) {
            documentToJson(it as Document)
        } else {
            encodeToString(schema, it)
        }
    }
}

suspend fun <T : Any> CoroutineCollection<T>.deleteOne(filter: String, options: DeleteOptions = DeleteOptions(), clientSession: ClientSession? = null): DeleteResult {
    return if (clientSession == null) {
        deleteOne(filter, options)
    } else {
        deleteOne(clientSession, filter, options)
    }
}

suspend fun <T : Any> CoroutineCollection<T>.deleteMany(filter: String, options: DeleteOptions = DeleteOptions(), clientSession: ClientSession? = null): DeleteResult {
    return if (clientSession == null) {
        deleteMany(filter, options)
    } else {
        deleteMany(clientSession, filter, options)
    }
}

suspend inline fun <reified T : Any> CoroutineCollection<T>.save(documentString: String, clientSession: ClientSession? = null): Any {
    return save(T::class, documentString, clientSession)
}

@Suppress("UNCHECKED_CAST")
suspend fun <T : Any> CoroutineCollection<T>.save(schema: KClass<T>, documentString: String, clientSession: ClientSession? = null): Any {
    val document = if (schema == Document::class) {
        jsonToDocument(documentString) as T
    } else {
        decodeFromString(schema, documentString)
    }
    val id = KMongoUtil.getIdValue(document)
    return if (id != null) {
        if (clientSession == null) {
            replaceOneById(id, document, ReplaceOptions().upsert(true))
        } else {
            replaceOneById(clientSession, id, document, ReplaceOptions().upsert(true))
        }
    } else {
        if (clientSession == null) {
            insertOne(document)
        } else {
            insertOne(clientSession, document)
        }
    }
}

suspend inline fun <reified T : Any> CoroutineCollection<T>.bulkWrite(requests: List<String>, options: BulkWriteOptions = BulkWriteOptions(), clientSession: ClientSession? = null): BulkWriteResult {
    return bulkWrite(T::class, requests, options, clientSession)
}

suspend fun <T : Any> CoroutineCollection<T>.bulkWrite(schema: KClass<T>, requests: List<String>, options: BulkWriteOptions = BulkWriteOptions(), clientSession: ClientSession? = null): BulkWriteResult {
    return if (clientSession == null) {
        withDocumentClass<BsonDocument>().bulkWrite(
            KMongoUtil.toWriteModel(
                requests.toTypedArray(),
                codecRegistry,
                schema
            ),
            options
        )
    } else {
        withDocumentClass<BsonDocument>().bulkWrite(
            clientSession,
            KMongoUtil.toWriteModel(
                requests.toTypedArray(),
                codecRegistry,
                schema
            ),
            options
        )
    }
}

inline fun <reified T : Any> CoroutineCollection<T>.projection(
    projection: String,
    query: String = EMPTY_JSON,
    noinline options: (CoroutineFindPublisher<MultipleProjection<Map<String, Any>>>) -> Unit = {},
    clientSession: ClientSession? = null
): CoroutineFindPublisher<MultipleProjection<Map<String, Any>>> {
    return projection(T::class, projection, query, options, clientSession)
}

@Suppress("UNCHECKED_CAST")
fun <T : Any> CoroutineCollection<T>.projection(
    schema: KClass<T>,
    projection: String,
    query: String = EMPTY_JSON,
    options: (CoroutineFindPublisher<MultipleProjection<Map<String, Any>>>) -> Unit = {},
    clientSession: ClientSession? = null
): CoroutineFindPublisher<MultipleProjection<Map<String, Any>>> {
    if (projection.isNotBlank()) {
        val projectionMap = objectMapper.readValue(projection, Map::class.java).filter { (key, value) ->
            key is String && value is Int
        } as Map<String, Int>
        return if (projectionMap.isNotEmpty()) {
            val newProjectionMap = mutableMapOf<String, Int>()
            if (schema == Document::class) {
                newProjectionMap.putAll(projectionMap)
                if (!projectionMap.keys.contains("_id")) {
                    newProjectionMap["_id"] = 0
                }
                val newProjection = objectMapper.writeValueAsString(newProjectionMap)
                if (clientSession == null) {
                    find(query)
                } else {
                    find(clientSession, jsonToBson(query))
                }
                    .publisher
                    .map {
                        MultipleProjection(it as Map<String, Any>)
                    }
                    .coroutine
                    .also { options(it) }
                    .projection(jsonToBson(newProjection))
            } else {
                var existId = false
                val properties = projectionMap.mapNotNull { (key, value) ->
                    schema.memberProperties.find {
                        it.name == key
                    }?.let {
                        if (it.path() == "_id") {
                            existId = true
                        }
                        newProjectionMap[it.path()] = value as Int
                        Pair(
                            it.path(),
                            it.returnType.classifier as KClass<*>
                        )
                    } ?: throw IllegalArgumentException("The field name '$key' does not match any property in the class ${schema.simpleName}.")
                }
                if (!existId) {
                    newProjectionMap["_id"] = 0
                }
                val newProjection = objectMapper.writeValueAsString(newProjectionMap)
                withDocumentClass<MultipleProjection<Map<String, Any>>>()
                    .withCodecRegistry(multipleProjectionCodecRegistry(properties, codecRegistry))
                    .run {
                        if (clientSession == null) {
                            find(query)
                        } else {
                            find(clientSession, jsonToBson(query))
                        }
                    }
                    .also { options(it) }
                    .projection(jsonToBson(newProjection))
            }
        } else {
            throw IllegalArgumentException("The provided projection map is empty. Please provide at least one field to project.")
        }
    } else {
        throw IllegalArgumentException("The provided projection is blank.")
    }
}

suspend inline fun <reified T : Any> CoroutineCollection<T>.projectionAsStringList(
    projection: String,
    query: String = EMPTY_JSON,
    noinline options: (CoroutineFindPublisher<MultipleProjection<Map<String, Any>>>) -> Unit = {},
    clientSession: ClientSession? = null
): List<String> {
    return projectionAsStringList(T::class, projection, query, options, clientSession)
}

suspend fun <T : Any> CoroutineCollection<T>.projectionAsStringList(
    schema: KClass<T>,
    projection: String,
    query: String = EMPTY_JSON,
    options: (CoroutineFindPublisher<MultipleProjection<Map<String, Any>>>) -> Unit = {},
    clientSession: ClientSession? = null
): List<String> {
    return projection(schema, projection, query, options, clientSession).toList().map {
        if (it.data is Document) {
            documentToJson(it.data)
        } else {
            objectMapper.writeValueAsString(it.data)
        }
    }
}
