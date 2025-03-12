package com.zmkn.module.kmongo.util

import com.fasterxml.jackson.databind.SerializationFeature
import com.mongodb.client.model.Filters
import com.zmkn.bson.codec.datetime.DatetimeBsonCodec
import com.zmkn.service.JacksonService
import com.zmkn.service.SerializationService
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.internal.FormatLanguage
import kotlinx.serialization.serializer
import org.bson.BsonDocument
import org.bson.Document
import org.bson.codecs.Decoder
import org.bson.codecs.Encoder
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.conversions.Bson
import org.bson.json.JsonWriterSettings
import org.litote.kmongo.id.jackson.IdJacksonModule
import org.litote.kmongo.id.serialization.IdKotlinXSerializationModule
import org.litote.kmongo.serialization.SerializationClassMappingTypeService
import org.litote.kmongo.service.ClassMappingType
import org.litote.kmongo.util.KMongoUtil.defaultCodecRegistry
import org.litote.kmongo.util.ObjectMappingConfiguration
import kotlin.LazyThreadSafetyMode.PUBLICATION
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.starProjectedType

object KMongoUtils {
    init {
        registerCustomCodec()
    }

    private val _internalDefaultRegistry: CodecRegistry by lazy(PUBLICATION) {
        CodecRegistries.fromCodecs()
        ClassMappingType.codecRegistry(defaultCodecRegistry)
    }

    const val KMONGO_MAPPING_SERVICE = "org.litote.mongo.mapping.service"
    const val EMPTY_JSON: String = "{}"
    val EMPTY_BSON: Bson = Filters.empty()

    val json = SerializationService {
        prettyPrint = false
        serializersModule = IdKotlinXSerializationModule
    }.json

    val objectMapper = JacksonService(initializer = {
        disable(SerializationFeature.INDENT_OUTPUT)
    }).objectMapper.registerModule(IdJacksonModule())

    fun isJsonArray(json: String) = json.trim().startsWith('[')

    fun registerCustomCodec() {
        // 注册自定义 Codec 编解码器
        ObjectMappingConfiguration.apply {
            DatetimeBsonCodec.all.forEach {
                addCustomCodec(it)
            }
        }
    }

    fun registerKMongoMappingService() {
        // 使用 KMongo Kotlin Serialization 运行时未导入 org.litote.kmongo:kmongo-serialization-mapping 库时需要设置以下属性以支持字段和类型的映射支持，用于解决 @SerialName 等注解和 Id 等类型序列化和反序列化无效问题，使用 shadow 插件打的 Jar 包中即使包含了 kmongo-serialization-mapping 库也需要设置，因为需要在运行时的项目导入才可以不用设置。
        System.getProperty(KMONGO_MAPPING_SERVICE)?.also {
            System.clearProperty(KMONGO_MAPPING_SERVICE)
        }
        System.setProperty(KMONGO_MAPPING_SERVICE, SerializationClassMappingTypeService::class.qualifiedName!!)
    }

    fun jsonToBson(jsonString: String): BsonDocument {
        return BsonDocument.parse(jsonString)
    }

    fun bsonToJson(bson: Bson): String {
        return bsonToJson(
            bson.toBsonDocument(
                Document::class.java,
                _internalDefaultRegistry
            )
        )
    }

    fun bsonToJson(bsonDocument: BsonDocument): String {
        return bsonDocument.toJson()
    }

    fun documentToJson(document: Document): String {
        return document.toJson()
    }

    fun documentToJson(document: Document, writerSettings: JsonWriterSettings): String {
        return document.toJson(writerSettings)
    }

    fun documentToJson(document: Document, encoder: Encoder<Document>): String {
        return document.toJson(encoder)
    }

    fun documentToJson(document: Document, writerSettings: JsonWriterSettings, encoder: Encoder<Document>): String {
        return document.toJson(writerSettings, encoder)
    }

    fun jsonToDocument(json: String): Document {
        return Document.parse(json)
    }

    fun jsonToDocument(json: String, decoder: Decoder<Document>): Document {
        return Document.parse(json, decoder)
    }

    inline fun <reified T> encodeToString(value: T): String {
        return json.encodeToString(value)
    }

    fun <T> encodeToString(kType: KType, value: T): String {
        return json.encodeToString(json.serializersModule.serializer(kType), value)
    }

    fun <T : Any> encodeToString(schema: KClass<T>, value: T): String {
        return encodeToString(schema.starProjectedType, value)
    }

    inline fun <reified T> decodeFromString(jsonString: String): T {
        return json.decodeFromString<T>(jsonString)
    }

    @Suppress("UNCHECKED_CAST")
    @OptIn(InternalSerializationApi::class)
    fun <T> decodeFromString(kType: KType, @FormatLanguage("json", "", "") jsonString: String): T {
        return json.decodeFromString(json.serializersModule.serializer(kType), jsonString) as T
    }

    @OptIn(InternalSerializationApi::class)
    fun <T : Any> decodeFromString(schema: KClass<T>, @FormatLanguage("json", "", "") jsonString: String): T {
        return decodeFromString(schema.starProjectedType, jsonString)
    }
}
