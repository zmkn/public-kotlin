import com.fasterxml.jackson.databind.node.ObjectNode
import com.mongodb.MongoNamespace
import com.mongodb.client.model.*
import com.mongodb.client.result.InsertOneResult
import com.mongodb.client.result.UpdateResult
import com.zmkn.extension.filter
import com.zmkn.module.kmongo.KMongo
import com.zmkn.module.kmongo.extension.*
import com.zmkn.module.kmongo.util.KMongoUtils
import com.zmkn.service.LoggerService
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import model.Account
import model.Name
import model.User
import org.bson.Document
import org.bson.types.ObjectId
import org.junit.jupiter.api.Disabled
import org.litote.kmongo.*
import org.litote.kmongo.util.KMongoUtil
import kotlin.test.Test

class KMongoTest {
    private val logger = LoggerService.getInstance()

    private val user = ""
    private val password = ""
    private val hosts = listOf("39.106.11.144:27017", "39.106.11.144:27018", "39.106.11.144:27019", "39.106.11.144:27020")
    private val database = "usercenter"
    private val replicaName = "rs0"
    private val connectionString = "mongodb://$user:$password@${hosts.joinToString(",")}/$database?authSource=$database&replicaSet=$replicaName"
    private val kMongo: KMongo by lazy {
        KMongo(connectionString, database)
    }

    @Serializable
    data class IndexInfo(
        val map: LinkedHashMap<String, Int> = linkedMapOf("a" to 1),
    )

    @Test
    @Disabled
    fun testDropCollection() = runBlocking {
        logger.error("testDropCollection---Start")
        val collection = kMongo.getCollection(User::class)
        collection.dropCollection()
        logger.error("testDropCollection---End")
    }

    @Test
    @Disabled
    fun testRenameCollection() = runBlocking {
        logger.error("testRenameCollection---Start")
        val collection = kMongo.getCollection(User::class)
        collection.renameCollection(MongoNamespace(kMongo.database.name, "user1"), RenameCollectionOptions(), null)
        logger.error("testRenameCollection---End")
    }

    @Test
    @Disabled
    fun testDropIndex() = runBlocking {
        logger.error("testDropIndex---Start")
        val collection = kMongo.getCollection(User::class)
        val result = collection.dropIndex("a", DropIndexOptions(), null)
        logger.error(result)
        logger.error("testDropIndex---End")
    }

    @Test
    @Disabled
    fun testDistinct() = runBlocking {
        logger.error("testDistinct---Start")
        val collection = kMongo.getCollection(User::class)
        val result = collection.distinctByField(Document::class, "createdAt")
        logger.error(result.toList())
        logger.error("testDistinct---End")
    }

    @Test
    @Disabled
    fun testListIndexes() = runBlocking {
        logger.error("kMongoText---Start")
        val collection = kMongo.getCollection(User::class)
        val listIndexes = collection.listIndexes(null)
        logger.error(listIndexes.toStringList())
        logger.error("kMongoText---End")
    }

    @Test
    @Disabled
    fun testFind() = runBlocking {
        logger.error("kMongoText---Start")
        val collection = kMongo.getCollection(User::class)
        val result = collection.find("""{}""")
        logger.error(result.toList())
        logger.error(result.toStringList())
        logger.error("kMongoText---End")
    }

    @Test
    @Disabled
    fun testFindOne() = runBlocking {
        logger.error("testFindOne---Start")
        val id = "67d11f7cff643220b30f9c50"
        val collection = kMongo.getCollection("user")
        val bson = User::id eq ObjectId(id)
        val bsonJson = KMongoUtils.bsonToJson(bson)
        logger.error(bsonJson)
        val user = collection.findOneAsString(Document::class, bsonJson)
        logger.error(user)
        logger.error("testFindOne---End")
    }

    @Test
    @Disabled
    fun testFindOneByDocument() = runBlocking {
        logger.error("testFindOneByDocument---Start")
        val collection = kMongo.getCollection("user")
        val result = collection.findOneAsString(Document::class, """{}""")
        logger.error(result)
        logger.error("testFindOneByDocument---End")
    }

    @Test
    @Disabled
    fun testAggregate() = runBlocking {
        logger.error("testAggregate---Start")
        val collection = kMongo.getCollection("user")
        val result = collection.aggregate(Document::class, listOf("{\$limit:2}"))
//        println(result.toList())
        logger.error(result.toStringList())
        logger.error("testAggregate---End")
    }

    @Test
    @Disabled
    fun testAggregateByDocument() = runBlocking {
        logger.error("testAggregateByDocument---Start")
        val userCollectionName = KMongoUtils.getCollectionName(User::class)
        val bsonList = listOf(
            match(
                Account::id eq ObjectId("67d11287d0f1c354bbad4c1e"),
            ),
            lookup(
                from = userCollectionName,
                localField = Account::userId.path(),
                foreignField = User::id.path(),
                newAs = "userId"
            ),
            unwind("\$userId", UnwindOptions().preserveNullAndEmptyArrays(true)),
        )
        println(bsonList)
        val pipeline = bsonList.map {
            KMongoUtils.bsonToJson(it)
        }
        println(pipeline)
        val bsonList2 = KMongoUtil.toBsonList(pipeline.toTypedArray(), KMongoUtils.customCodecRegistry)
        println(bsonList2)
        val collection = kMongo.getCollection(Account::class, Document::class)
        val result = collection.aggregate(Document::class, pipeline)
        logger.error(result.toStringList(Document::class))
        logger.error("testAggregateByDocument---End")
    }

    @Test
    @Disabled
    fun testInsertOne() = runBlocking {
        logger.error("kMongoText---Start")
        val collection = kMongo.getCollection(User::class)
        val user = User(
            accountId = ObjectId(),
            nickName = "xz",
            profilePictureUrl = "qq.com",
            status = "DISABLED",
            phoneNumbers = emptyList(),
        )
        println(user)
        val userJson = KMongoUtils.encodeToString(user)
        println(userJson)
        println(user.json)
        println("start mongodb insert")
        val result = collection.insertOne(Document::class, userJson)
        logger.error(result)
        logger.error("kMongoText---End")
    }

    @Test
    @Disabled
    fun testInsertOneByDocument() = runBlocking {
        logger.error("kMongoText---Start")
        val collection = kMongo.getCollection("user")
        val user = Document(
            mapOf(
                "accountId" to ObjectId(),
                "nickName" to "xz",
                "profilePictureUrl" to "qq.com",
                "status" to "DISABLED",
                "phoneNumbers" to emptyList<String>(),
            )
        )
        println(user)
        val userJson = KMongoUtils.documentToJson(user)
        println(userJson)
        val result = collection.insertOne(Document::class, userJson)
        logger.error(result)
        logger.error("kMongoText---End")
    }

    @Test
    @Disabled
    fun testFindOneAndUpdate() = runBlocking {
        logger.error("testFindOneAndUpdate---Start")
        val collection = kMongo.getCollection(User::class)
        val filterBson = User::id eq ObjectId("67a41a362722ff2ddd47a7e8")
        val filter = KMongoUtils.bsonToJson(filterBson)
        println(filter)
        val user = User(
            accountId = ObjectId(),
            nickName = "testFindOneAndUpdate",
            profilePictureUrl = "qq.com",
            status = "DISABLED",
            phoneNumbers = emptyList(),
        )
        val defaultDataSetObjectNode = KMongoUtils.objectMapper.valueToTree<ObjectNode>(user).filter {
            it != User::id.path() && it != User::createdAt.path()
        }
        val userSetJson = KMongoUtils.objectMapper.writeValueAsString(defaultDataSetObjectNode)
        val userSetOnInsertObjectNode = KMongoUtils.objectMapper.valueToTree<ObjectNode>(user).filter {
            it == User::createdAt.path()
        }
        val userSetOnInsertJson = KMongoUtils.objectMapper.writeValueAsString(userSetOnInsertObjectNode)
        val update = "{ \$set: $userSetJson, \$setOnInsert: $userSetOnInsertJson }"
        println(update)
        val result = collection.findOneAndUpdateAsString(
            filter,
            update,
            FindOneAndUpdateOptions().upsert(true).returnDocument(ReturnDocument.AFTER),
        )
        logger.error(result)
        logger.error("testFindOneAndUpdate---End")
    }

    @Test
    @Disabled
    fun testFindOneAndReplace() = runBlocking {
        logger.error("testFindOneAndReplace---Start")
        val collection = kMongo.getCollection("user")
        val filter = "{\"_id\": \"67d266b14fe9e00f42d3edc1\"}"
        val filter1 = Filters.eq("_id", ObjectId("67d266b14fe9e00f42d3edc1"))
        println(filter1)
        println(filter)
        println(KMongoUtils.bsonToJson(filter1))
        val user = User(
            id = ObjectId("67d266b14fe9e00f42d3edc1"),
            accountId = ObjectId(),
            nickName = "aaa",
            profilePictureUrl = "taobao.com",
            status = "DISABLED",
            phoneNumbers = emptyList(),
        )
        val replacement = KMongoUtils.encodeToString(user)
        println(replacement)
        val result = collection.findOneAndReplaceAsString(filter, replacement)
        logger.error(result)
        logger.error("testFindOneAndReplace---End")
    }

    @Test
    @Disabled
    fun testFindOneAndDelete() = runBlocking {
        logger.error("kMongoText---Start")
        val collection = kMongo.getCollection(User::class)
        val filter = "{\"_id\": {\"\$oid\": \"6773ac7ce872ed3293b09fe1\"}}"
        val filter1 = Filters.eq("_id", ObjectId("6773ac7ce872ed3293b09fe1"))
        println(filter1)
        println(filter)
        println(KMongoUtils.bsonToJson(filter1))
        val result = collection.findOneAndDeleteAsString(filter)
        logger.error(result)
        logger.error("kMongoText---End")
    }

    @Test
    @Disabled
    fun testSave() = runBlocking {
        logger.error("testSave---Start")
        val collection = kMongo.getCollection(User::class)
        val user = User(
            id = ObjectId("67739a2cbbc61977be08b47c"),
            accountId = ObjectId(),
            nickName = "kz",
            profilePictureUrl = "taobao.com",
            status = "DISABLED",
            phoneNumbers = emptyList(),
        )
        println(user)
        val userJson = KMongoUtils.encodeToString(user)
        println(userJson)
        println(user.json)
        println("start mongodb save")
        val result = collection.save(userJson)
        logger.error(result)
        if (result is UpdateResult) {
            println(result.wasAcknowledged())
        } else if (result is InsertOneResult) {
            println("bbbbbbbbbbb")
        }
        logger.error("testSave---End")
    }

    @Test
    @Disabled
    fun testBulkWrite() = runBlocking {
        logger.error("testBulkWrite---Start")
        val collection = kMongo.getCollection(User::class)
        val user = User(
            accountId = ObjectId(),
            nickName = "testBulkWrite",
            profilePictureUrl = "baidu.com",
            status = "DISABLED",
            phoneNumbers = emptyList(),
        )
        println(user)
        val userJson = KMongoUtils.encodeToString(user)
        println(userJson)
        println(user.json)
        val requests = listOf("""{ insertOne : $userJson }""")
        println(requests)
        val result = collection.bulkWrite(requests)
        println(result)
        logger.error("testBulkWrite---End")
    }

    @Test
    @Disabled
    fun testBulkWriteByDocument() = runBlocking {
        logger.error("testBulkWriteByDocument---Start")
        val collection = kMongo.getCollection("user")
        val user = User(
            accountId = ObjectId(),
            nickName = "testBulkWrite",
            profilePictureUrl = "baidu.com",
            status = "DISABLED",
            phoneNumbers = emptyList(),
        )
        println(user)
        val userJson = KMongoUtils.encodeToString(user)
        println(userJson)
        val requests = listOf("""{ insertOne : $userJson }""")
        println(requests)
        val result = collection.bulkWrite(Document::class, requests)
        println(result)
        logger.error("testBulkWriteByDocument---End")
    }

    @Test
    @Disabled
    fun testProjection() = runBlocking {
        logger.error("kMongoText---Start")
        val collection = kMongo.getCollection(User::class)
        println(excludeId().json)
        val result = collection.projectionAsStringList(Document::class, """{"_id":0,"accountId":1,"nickName":1}""", """{}""")
        logger.error(result)
        logger.error("kMongoText---End")
    }

    @Test
    @Disabled
    fun testProjectionByDocument() = runBlocking {
        logger.error("testProjectionByDocument---Start")
        val collection = kMongo.getCollection(User::class)
        val result = collection.projectionAsStringList(Document::class, """{"_id":0,"accountId":1,"nickName":1}""", """{}""")
        logger.error(result)
        logger.error("testProjectionByDocument---End")
    }

    @Test
    @Disabled
    fun testBsonToJson() {
        val updateBson = combine(
            setValue(Account::account, "aaaabbbb"),
            setValue(Account::accountUpdatedAt, Clock.System.now()),
        )
        println(updateBson)
        val updateJson = KMongoUtils.bsonToJson(updateBson)
        println(updateJson)
    }

    @Test
    @Disabled
    fun testGetCollectionName() {
        val collectionName = KMongoUtils.getCollectionName(Account::class)
        println(collectionName)
    }

    @Test
    @Disabled
    fun testDecodeFromDocument() {
        val name = Name(
            name = "name111",
        )
        val json = KMongoUtils.encodeToString(name)
        println(json)
        val document = KMongoUtils.jsonToDocument(json)
        println(document)
        println(document["_id"] is ObjectId)
        println(KMongoUtils.decodeFromDocument(Name::class, document))
        val json2 = KMongoUtils.documentToJson(document)
        println(json2)
        println(KMongoUtils.decodeFromString(Name::class, json2))
    }
}
