import com.fasterxml.jackson.databind.node.ObjectNode
import com.mongodb.MongoNamespace
import com.mongodb.client.model.*
import com.mongodb.client.result.InsertOneResult
import com.mongodb.client.result.UpdateResult
import com.zmkn.extension.filter
import com.zmkn.module.kmongo.KMongo
import com.zmkn.module.kmongo.extension.*
import com.zmkn.module.kmongo.util.KMongoUtils
import kotlinx.coroutines.runBlocking
import model.Account
import model.Name
import model.User
import org.bson.Document
import org.bson.types.ObjectId
import org.junit.jupiter.api.Disabled
import org.litote.kmongo.*
import org.litote.kmongo.util.KMongoUtil
import kotlin.reflect.typeOf
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class KMongoTest {
    private val user = ""
    private val password = ""
    private val hosts = listOf("39.106.11.144:27017", "39.106.11.144:27018", "39.106.11.144:27019", "39.106.11.144:27020")
    private val database = "usercenter"
    private val replicaName = "rs0"
    private val connectionString = "mongodb://$user:$password@${hosts.joinToString(",")}/$database?authSource=$database&replicaSet=$replicaName"
    private val kMongo: KMongo by lazy {
        KMongo(connectionString, database)
    }

    @Test
    @Disabled
    fun testDropCollection() = runBlocking {
        println("testDropCollection---Start")
        val collection = kMongo.getCollection(User::class)
        collection.dropCollection()
        println("testDropCollection---End")
    }

    @Test
    @Disabled
    fun testRenameCollection() = runBlocking {
        println("testRenameCollection---Start")
        val collection = kMongo.getCollection(User::class)
        collection.renameCollection(MongoNamespace(kMongo.database.name, "user1"), RenameCollectionOptions(), null)
        println("testRenameCollection---End")
    }

    @Test
    @Disabled
    fun testDropIndex() = runBlocking {
        println("testDropIndex---Start")
        val collection = kMongo.getCollection(User::class)
        val result = collection.dropIndex("a", DropIndexOptions(), null)
        println(result)
        println("testDropIndex---End")
    }

    @Test
    @Disabled
    fun testDistinct() = runBlocking {
        println("testDistinct---Start")
        val collection = kMongo.getCollection(User::class)
        val result = collection.distinctByField(Document::class, "createdAt")
        println(result.toList())
        println("testDistinct---End")
    }

    @Test
    @Disabled
    fun testListIndexes() = runBlocking {
        println("kMongoText---Start")
        val collection = kMongo.getCollection(User::class)
        val listIndexes = collection.listIndexes(null)
        println(listIndexes.toStringList())
        println("kMongoText---End")
    }

    @Test
    @Disabled
    fun testFind() = runBlocking {
        println("kMongoText---Start")
        val collection = kMongo.getCollection(User::class)
        val result = collection.find("""{}""")
        println(result.toList())
        println(result.toStringList())
        println("kMongoText---End")
    }

    @Test
    @Disabled
    fun testFindOne() = runBlocking {
        println("testFindOne---Start")
        val id = "67d11f7cff643220b30f9c50"
        val collection = kMongo.getCollection("user")
        val bson = User::id eq ObjectId(id)
        val bsonJson = KMongoUtils.bsonToJson(bson)
        println(bsonJson)
        val user = collection.findOneAsString(Document::class, bsonJson)
        println(user)
        println("testFindOne---End")
    }

    @Test
    @Disabled
    fun testFindOneByDocument() = runBlocking {
        println("testFindOneByDocument---Start")
        val collection = kMongo.getCollection("user")
        val result = collection.findOneAsString(Document::class, """{}""")
        println(result)
        println("testFindOneByDocument---End")
    }

    @Test
    @Disabled
    fun testAggregate() = runBlocking {
        println("testAggregate---Start")
        val collection = kMongo.getCollection("user")
        val result = collection.aggregate(Document::class, listOf($$"{$limit:2}"))
//        println(result.toList())
        println(result.toStringList())
        println("testAggregate---End")
    }

    @Test
    @Disabled
    fun testAggregateByDocument() = runBlocking {
        println("testAggregateByDocument---Start")
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
            unwind($$"$userId", UnwindOptions().preserveNullAndEmptyArrays(true)),
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
        println(result.toStringList(Document::class))
        println("testAggregateByDocument---End")
    }

    @OptIn(ExperimentalTime::class)
    @Test
    @Disabled
    fun testInsertOne() = runBlocking {
        println("kMongoText---Start")
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
        println(result)
        println("kMongoText---End")
    }

    @Test
    @Disabled
    fun testInsertOneByDocument() = runBlocking {
        println("kMongoText---Start")
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
        println(result)
        println("kMongoText---End")
    }

    @OptIn(ExperimentalTime::class)
    @Test
    @Disabled
    fun testFindOneAndUpdate() = runBlocking {
        println("testFindOneAndUpdate---Start")
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
        val update = $$"{ $set: $$userSetJson, $setOnInsert: $$userSetOnInsertJson }"
        println(update)
        val result = collection.findOneAndUpdateAsString(
            filter,
            update,
            FindOneAndUpdateOptions().upsert(true).returnDocument(ReturnDocument.AFTER),
        )
        println(result)
        println("testFindOneAndUpdate---End")
    }

    @OptIn(ExperimentalTime::class)
    @Test
    @Disabled
    fun testFindOneAndReplace() = runBlocking {
        println("testFindOneAndReplace---Start")
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
        println(result)
        println("testFindOneAndReplace---End")
    }

    @Test
    @Disabled
    fun testFindOneAndDelete() = runBlocking {
        println("kMongoText---Start")
        val collection = kMongo.getCollection(User::class)
        val filter = $$"{\"_id\": {\"$oid\": \"6773ac7ce872ed3293b09fe1\"}}"
        val filter1 = Filters.eq("_id", ObjectId("6773ac7ce872ed3293b09fe1"))
        println(filter1)
        println(filter)
        println(KMongoUtils.bsonToJson(filter1))
        val result = collection.findOneAndDeleteAsString(filter)
        println(result)
        println("kMongoText---End")
    }

    @OptIn(ExperimentalTime::class)
    @Test
    @Disabled
    fun testSave() = runBlocking {
        println("testSave---Start")
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
        println(result)
        if (result is UpdateResult) {
            println(result.wasAcknowledged())
        } else if (result is InsertOneResult) {
            println("bbbbbbbbbbb")
        }
        println("testSave---End")
    }

    @OptIn(ExperimentalTime::class)
    @Test
    @Disabled
    fun testBulkWrite() = runBlocking {
        println("testBulkWrite---Start")
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
        println("testBulkWrite---End")
    }

    @OptIn(ExperimentalTime::class)
    @Test
    @Disabled
    fun testBulkWriteByDocument() = runBlocking {
        println("testBulkWriteByDocument---Start")
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
        println("testBulkWriteByDocument---End")
    }

    @Test
    @Disabled
    fun testProjection() = runBlocking {
        println("kMongoText---Start")
        val collection = kMongo.getCollection(User::class)
        println(excludeId().json)
        val result = collection.projectionAsStringList(Document::class, """{"_id":0,"accountId":1,"nickName":1}""", """{}""")
        println(result)
        println("kMongoText---End")
    }

    @Test
    @Disabled
    fun testProjectionByDocument() = runBlocking {
        println("testProjectionByDocument---Start")
        val collection = kMongo.getCollection(User::class)
        val result = collection.projectionAsStringList(Document::class, """{"_id":0,"accountId":1,"nickName":1}""", """{}""")
        println(result)
        println("testProjectionByDocument---End")
    }

    @OptIn(ExperimentalTime::class)
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

    @OptIn(ExperimentalTime::class)
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

    @OptIn(ExperimentalTime::class)
    @Test
//    @Disabled
    fun testDecodeFromString() {
        val account = Account(
            id = ObjectId("67d11287d0f1c354bbad4c1e"),
            notificationGroupId = ObjectId("67d11288d0f1c354bbad4c1f"),
            account = "aaabbbccc",
            phoneNumber = "16601190129",
            status = "NORMAL",
            userId = ObjectId("67d11287d0f1c354bbad4c1d"),
            passwordStatus = "NORMAL",
            accountUpdatedAt = Instant.parse("2025-03-20T02:17:13.938030200Z"),
            password = "null",
            passwordUpdatedAt = Instant.parse("2025-03-12T04:50:16.362275400Z"),
            passwordUpdatedVersion = 1,
            createdAt = Instant.parse("2025-03-12T04:50:16.362275400Z"),
            updatedAt = Instant.parse("2025-03-12T04:50:16.362275400Z"),
        )
        val kType = typeOf<Account>()
//        val json = KMongoUtils.encodeToString(kType, account)
        val json: String = $$"""{"_id": {"$oid": "67d11287d0f1c354bbad4c1e"}, "notificationGroupId": {"$oid": "67d11288d0f1c354bbad4c1f"}, "account": "aaabbbccc", "phoneNumber": "16601190129", "status": "NORMAL", "userId": {"$oid": "67d11287d0f1c354bbad4c1d"}, "passwordStatus": "NORMAL", "accountUpdatedAt": "2025-03-20T02:17:13.938030200Z", "password": "$argon2id$v=19$m=20480,t=20,p=4$c0yM4V4dyQwebDr0srnKQA$YaQGMkazKmHqithwjjshRsnwWVROOtki6RmHIxfn6M9keU+iJJ6nSPQkohwXEI7fc86iewQFWQnj0b5he+mHN8c90emF0xkgSGhkKj1/nAyhLRo/IU7E9ZnLo6U8s/1fZ9Y1Ym0HbR2ztoie8yf9t9PRxawT81ySq4TxQ7aFkXc", "passwordUpdatedAt": "2025-03-12T04:50:16.362275400Z", "passwordUpdatedVersion": 1, "createdAt": "2025-03-12T04:50:16.362275400Z", "updatedAt": "2025-03-12T04:50:16.362275400Z"}"""
        println(json)
        val accountData = KMongoUtils.decodeFromString<Account>(kType, json)
        println(accountData)
    }
}
