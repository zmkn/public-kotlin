import com.mongodb.MongoNamespace
import com.mongodb.client.model.DropIndexOptions
import com.mongodb.client.model.Filters
import com.mongodb.client.model.RenameCollectionOptions
import com.mongodb.client.model.UnwindOptions
import com.mongodb.client.result.InsertOneResult
import com.mongodb.client.result.UpdateResult
import com.zmkn.module.kmongo.KMongo
import com.zmkn.module.kmongo.extension.*
import com.zmkn.module.kmongo.util.KMongoUtils.bsonToJson
import com.zmkn.module.kmongo.util.KMongoUtils.customCodecRegistry
import com.zmkn.module.kmongo.util.KMongoUtils.documentToJson
import com.zmkn.module.kmongo.util.KMongoUtils.encodeToString
import com.zmkn.module.kmongo.util.KMongoUtils.getCollectionName
import com.zmkn.service.LoggerService
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import model.Account
import model.User
import org.bson.Document
import org.bson.types.ObjectId
import org.junit.jupiter.api.Disabled
import org.litote.kmongo.*
import org.litote.kmongo.id.WrappedObjectId
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

    //    @Test
    fun testDropCollection() = runBlocking {
        logger.error("testDropCollection---Start")
        val collection = kMongo.getCollection("user", User::class)
        collection.dropCollection()
        logger.error("testDropCollection---End")
    }

    //    @Test
    fun testRenameCollection() = runBlocking {
        logger.error("testRenameCollection---Start")
        val collection = kMongo.getCollection("user", User::class)
        collection.renameCollection(MongoNamespace(kMongo.database.name, "user1"), RenameCollectionOptions(), null)
        logger.error("testRenameCollection---End")
    }

    //        @Test
    fun testDropIndex() = runBlocking {
        logger.error("testDropIndex---Start")
        val collection = kMongo.getCollection("user", User::class)
        val result = collection.dropIndex("a", DropIndexOptions(), null)
        logger.error(result)
        logger.error("testDropIndex---End")
    }

    //    @Test
    fun testDistinct() = runBlocking {
        logger.error("testDistinct---Start")
        val collection = kMongo.getCollection("user", User::class)
        val result = collection.distinctByField(User::class, "createdAt")
        logger.error(result.toList())
        logger.error("testDistinct---End")
    }

    //    @Test
    fun testListIndexes() = runBlocking {
        logger.error("kMongoText---Start")
        val collection = kMongo.getCollection("user", User::class)
        val listIndexes = collection.listIndexes(null)
        logger.error(listIndexes.toStringList())
        logger.error("kMongoText---End")
    }

    //    @Test
    fun testFind() = runBlocking {
        logger.error("kMongoText---Start")
        val collection = kMongo.getCollection("user", User::class)
        val result = collection.find("""{}""")
        logger.error(result.toList())
        logger.error("kMongoText---End")
    }

    //    @Test
    fun testFindOne() = runBlocking {
        logger.error("testFindOne---Start")
        val id = "67a4397dba298a37b00f5419"
        val collection = kMongo.getCollection("user", User::class)
        val bson = User::id eq WrappedObjectId(id)
        logger.error(bson)
        val user = collection.collection.find(bson).awaitFirstOrNull()
        logger.error(user)
//        val result = collection.findOneAsString(User::class, "{ \"${User::id.path()}\": { \"\$oid\": \"$id\" } }")
//        logger.error(result)
        logger.error("testFindOne---End")
    }

    //    @Test
    fun testFindOneByDocument() = runBlocking {
        logger.error("testFindOneByDocument---Start")
        val collection = kMongo.getCollection("user")
        val result = collection.findOneAsString(Document::class, """{}""")
        logger.error(result)
        logger.error("testFindOneByDocument---End")
    }

    //    @Test
    fun testAggregate() = runBlocking {
        logger.error("testAggregate---Start")
        val collection = kMongo.getCollection("user", User::class)
        val result = collection.aggregate(User::class, listOf("{\$limit:2}"))
        logger.error(result.toList())
        logger.error("testAggregate---End")
    }

    @Test
    fun testAggregateByDocument() = runBlocking {
        logger.error("testAggregateByDocument---Start")
        val userCollectionName = getCollectionName(User::class)
        val bsonList = listOf(
            match(
                Account::id eq WrappedObjectId("67d11287d0f1c354bbad4c1e"),
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
            bsonToJson(it)
        }
        println(pipeline)
        val bsonList2 = KMongoUtil.toBsonList(pipeline.toTypedArray(), customCodecRegistry)
        println(bsonList2)
        val collection = kMongo.getCollection(Account::class, Document::class)
        val result = collection.aggregate(Document::class, pipeline)
        logger.error(result.toStringList(Document::class))
        logger.error("testAggregateByDocument---End")
    }

    //    @Test
    fun testInsertOne() = runBlocking {
        logger.error("kMongoText---Start")
        val collection = kMongo.getCollection("user", User::class)
        val user = User(
            accountId = newId(),
            nickName = "xz",
            profilePictureUrl = "qq.com",
            status = "DISABLED",
            phoneNumbers = emptyList(),
        )
        println(user)
        val userJson = encodeToString(user)
        println(userJson)
        println(user.json)
        println("start mongodb insert")
        val result = collection.insertOne(User::class, userJson)
        logger.error(result)
        logger.error("kMongoText---End")
    }

    //    @Test
    fun testInsertOneByDocument() = runBlocking {
        logger.error("kMongoText---Start")
        val collection = kMongo.getCollection("user")
        val user = Document(
            mapOf(
                "accountId" to ObjectId().toString(),
                "nickName" to "xz",
                "profilePictureUrl" to "qq.com",
                "status" to "DISABLED",
                "phoneNumbers" to emptyList<String>(),
            )
        )
        println(user)
        val userJson = documentToJson(user)
        println(userJson)
        val result = collection.insertOne(Document::class, userJson)
        logger.error(result)
        logger.error("kMongoText---End")
    }

    //    @Test
    fun testFindOneAndUpdate() = runBlocking {
        logger.error("testFindOneAndUpdate---Start")
        val collection = kMongo.getCollection("user", User::class)
        val filter = "{\"_id\": {\"\$oid\": \"677398c1295a1b7ee5dee99e\"}}"
        val filter1 = Filters.eq("_id", ObjectId("677398c1295a1b7ee5dee99e"))
        println(filter1)
        println(filter)
        println(bsonToJson(filter1))
        val update = "{\$set: { \"nickName\": \"xxxx\" }}"
        println(update)
        val result = collection.findOneAndUpdateAsString(filter, update)
        logger.error(result)
        logger.error("testFindOneAndUpdate---End")
    }

    //    @Test
    fun testFindOneAndReplace() = runBlocking {
        logger.error("testFindOneAndReplace---Start")
        val collection = kMongo.getCollection("user", User::class)
        val filter = "{\"_id\": {\"\$oid\": \"677398c1295a1b7ee5dee99e\"}}"
        val filter1 = Filters.eq("_id", ObjectId("677398c1295a1b7ee5dee99e"))
        println(filter1)
        println(filter)
        println(bsonToJson(filter1))
        val user = User(
            id = "677398c1295a1b7ee5dee99e".toId(),
            accountId = newId(),
            nickName = "aaa",
            profilePictureUrl = "taobao.com",
            status = "DISABLED",
            phoneNumbers = emptyList(),
        )
        val replacement = encodeToString(user)
        println(replacement)
        val result = collection.findOneAndReplaceAsString(filter, replacement)
        logger.error(result)
        logger.error("testFindOneAndReplace---End")
    }

    //    @Test
    fun testFindOneAndDelete() = runBlocking {
        logger.error("kMongoText---Start")
        val collection = kMongo.getCollection("user", User::class)
        val filter = "{\"_id\": {\"\$oid\": \"6773ac7ce872ed3293b09fe1\"}}"
        val filter1 = Filters.eq("_id", ObjectId("6773ac7ce872ed3293b09fe1"))
        println(filter1)
        println(filter)
        println(bsonToJson(filter1))
        val result = collection.findOneAndDeleteAsString(filter)
        logger.error(result)
        logger.error("kMongoText---End")
    }

    //    @Test
    fun testSave() = runBlocking {
        logger.error("testSave---Start")
        val collection = kMongo.getCollection("user", User::class)
        val user = User(
            id = "67739a2cbbc61977be08b47c".toId(),
            accountId = newId(),
            nickName = "kz",
            profilePictureUrl = "taobao.com",
            status = "DISABLED",
            phoneNumbers = emptyList(),
        )
        println(user)
        val userJson = encodeToString(user)
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

    //    @Test
    fun testBulkWrite() = runBlocking {
        logger.error("testBulkWrite---Start")
        val collection = kMongo.getCollection("user", User::class)
        val user = User(
            accountId = newId(),
            nickName = "testBulkWrite",
            profilePictureUrl = "baidu.com",
            status = "DISABLED",
            phoneNumbers = emptyList(),
        )
        println(user)
        val userJson = encodeToString(user)
        println(userJson)
        println(user.json)
        val requests = listOf("""{ insertOne : $userJson }""")
        println(requests)
        val result = collection.bulkWrite(requests)
        println(result)
        logger.error("testBulkWrite---End")
    }

    //    @Test
    fun testBulkWriteByDocument() = runBlocking {
        logger.error("testBulkWriteByDocument---Start")
        val collection = kMongo.getCollection("user")
        val user = User(
            accountId = newId(),
            nickName = "testBulkWrite",
            profilePictureUrl = "baidu.com",
            status = "DISABLED",
            phoneNumbers = emptyList(),
        )
        println(user)
        val userJson = encodeToString(user)
        println(userJson)
        val requests = listOf("""{ insertOne : $userJson }""")
        println(requests)
        val result = collection.bulkWrite(Document::class, requests)
        println(result)
        logger.error("testBulkWriteByDocument---End")
    }

    //        @Test
    fun testProjection() = runBlocking {
        logger.error("kMongoText---Start")
        val collection = kMongo.getCollection("user", User::class)
        println(excludeId().json)
        val result = collection.projectionAsStringList(User::class, """{"id":0,"accountId":1,"nickName":1}""")
        logger.error(result)
        logger.error("kMongoText---End")
    }

    //    @Test
    fun testProjectionByDocument() = runBlocking {
        logger.error("testProjectionByDocument---Start")
        val collection = kMongo.getCollection("user")
        val result = collection.projectionAsStringList(Document::class, """{"_id":0,"accountId":1,"nickName":1}""")
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
        val updateJson = bsonToJson(updateBson)
        println(updateJson)
    }

    @Test
    @Disabled
    fun testGetCollectionName() {
        val collectionName = getCollectionName(Account::class)
        println(collectionName)
    }
}
