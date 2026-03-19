package com.zmkn.module.kmongo.util

import com.mongodb.client.model.Filters
import com.zmkn.bson.codec.datetime.DatetimeBsonCodec
import com.zmkn.bson.codec.time.TimeBsonCodec
import com.zmkn.jackson.module.bson.BsonJacksonModule
import com.zmkn.kotlin.serializers.module.bson.BsonKotlinSerializersModule
import com.zmkn.serialization.jackson.Jackson
import com.zmkn.service.SerializationService
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.internal.FormatLanguage
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import org.bson.BsonDocument
import org.bson.Document
import org.bson.codecs.Decoder
import org.bson.codecs.Encoder
import org.bson.codecs.configuration.CodecRegistries
import org.bson.codecs.configuration.CodecRegistry
import org.bson.conversions.Bson
import org.bson.json.JsonWriterSettings
import org.litote.kmongo.SetTo
import org.litote.kmongo.path
import org.litote.kmongo.serialization.SerializationClassMappingTypeService
import org.litote.kmongo.service.ClassMappingType
import org.litote.kmongo.util.KMongoUtil.defaultCodecRegistry
import org.litote.kmongo.util.ObjectMappingConfiguration
import tools.jackson.databind.SerializationFeature
import tools.jackson.databind.json.JsonMapper
import kotlin.LazyThreadSafetyMode.PUBLICATION
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.starProjectedType

object KMongoUtils {
    init {
        registerCustomCodec()
    }

    val customCodecRegistry: CodecRegistry by lazy(PUBLICATION) {
        CodecRegistries.fromCodecs()
        ClassMappingType.codecRegistry(defaultCodecRegistry)
    }

    const val KMONGO_MAPPING_SERVICE = "org.litote.mongo.mapping.service"
    const val EMPTY_JSON: String = "{}"
    val EMPTY_BSON: Bson = Filters.empty()

    val json = SerializationService {
        prettyPrint = false
        serializersModule = SerializersModule {
            include(BsonKotlinSerializersModule.all)
        }
    }.json

    val jsonMapper: JsonMapper = Jackson(initializer = {
        disable(SerializationFeature.INDENT_OUTPUT)
        addModule(BsonJacksonModule.all)
    }).jsonMapper

    fun isJsonArray(json: String) = json.trim().startsWith('[')

    fun registerCustomCodec() {
        // 注册自定义 Codec 编解码器
        ObjectMappingConfiguration.apply {
            DatetimeBsonCodec.all.forEach {
                addCustomCodec(it)
            }
            TimeBsonCodec.all.forEach {
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

    fun getCollectionName(fromClass: KClass<*>): String {
        val simpleName = fromClass.simpleName!!
        val collectionName =
            simpleName
                .toCharArray()
                .run {
                    foldIndexed(StringBuilder()) { i, s, c ->
                        s.append(
                            if (c.isUpperCase()) {
                                if (i == 0) {
                                    c.lowercaseChar()
                                } else {
                                    "_${c.lowercaseChar()}"
                                }
                            } else {
                                c
                            },
                        )
                    }.toString()
                }
        return collectionName
    }

    fun jsonToBson(jsonString: String): BsonDocument = BsonDocument.parse(jsonString)

    fun bsonToJson(bson: Bson): String = bsonToJson(
        bson.toBsonDocument(
            Document::class.java,
            customCodecRegistry
        )
    )

    fun bsonToJson(bsonDocument: BsonDocument): String = bsonDocument.toJson()

    fun documentToJson(document: Document): String = document.toJson()

    fun documentToJson(document: Document, writerSettings: JsonWriterSettings): String = document.toJson(writerSettings)

    fun documentToJson(document: Document, encoder: Encoder<Document>): String = document.toJson(encoder)

    fun documentToJson(document: Document, writerSettings: JsonWriterSettings, encoder: Encoder<Document>): String = document.toJson(writerSettings, encoder)

    fun jsonToDocument(json: String): Document = Document.parse(json)

    fun jsonToDocument(json: String, decoder: Decoder<Document>): Document = Document.parse(json, decoder)

    inline fun <reified T> encodeToString(value: T): String = json.encodeToString(value)

    fun <T> encodeToString(kType: KType, value: T): String = if (kType == Document::class.starProjectedType) {
        documentToJson(value as Document)
    } else {
        json.encodeToString(json.serializersModule.serializer(kType), value)
    }

    fun <T : Any> encodeToString(schema: KClass<T>, value: T): String = encodeToString(schema.starProjectedType, value)

    inline fun <reified T> encodeToDocument(value: T): Document {
        val json = encodeToString(value)
        return jsonToDocument(json)
    }

    fun <T> encodeToDocument(kType: KType, value: T): Document {
        val json = encodeToString(kType, value)
        return jsonToDocument(json)
    }

    fun <T : Any> encodeToDocument(schema: KClass<T>, value: T): Document = encodeToDocument(schema.starProjectedType, value)

    inline fun <reified T> decodeFromString(jsonString: String): T = json.decodeFromString(jsonString)

    @Suppress("UNCHECKED_CAST")
    @OptIn(InternalSerializationApi::class)
    fun <T> decodeFromString(kType: KType, @FormatLanguage("json", "", "") jsonString: String): T = if (kType == Document::class.starProjectedType) {
        jsonToDocument(jsonString) as T
    } else {
        json.decodeFromString(json.serializersModule.serializer(kType), jsonString) as T
    }

    @OptIn(InternalSerializationApi::class)
    fun <T : Any> decodeFromString(schema: KClass<T>, @FormatLanguage("json", "", "") jsonString: String): T = decodeFromString(schema.starProjectedType, jsonString)

    fun <T> decodeFromDocument(kType: KType, document: Document): T {
        val json = documentToJson(document)
        return decodeFromString(kType, json)
    }

    fun <T : Any> decodeFromDocument(schema: KClass<T>, document: Document): T = decodeFromDocument(schema.starProjectedType, document)

    inline fun <reified T> decodeFromDocument(document: Document): T = json.decodeFromString(documentToJson(document))

    fun <T : Any> formatAsPropertyList(
        model: T,
        excludedFields: Collection<String> = setOf(),
        allowedNull: Boolean = false,
    ): List<KProperty1<T, Any?>> = model::class.memberProperties.filterIsInstance<KProperty1<T, Any?>>()
        .let {
            if (excludedFields.isEmpty()) {
                it
            } else {
                it.filter { property ->
                    !excludedFields.contains(property.name)
                }
            }
        }
        .let {
            if (allowedNull) {
                it
            } else {
                it.filter { property ->
                    property.get(model) != null
                }
            }
        }

    fun <T : Any> generateEqList(
        model: T,
        excludedFields: Collection<String> = setOf(),
        allowedNull: Boolean = false,
    ): List<Bson> = formatAsPropertyList(
        model = model,
        excludedFields = excludedFields,
        allowedNull = allowedNull,
    ).map { property ->
        Filters.eq(property.path(), property.get(model))
    }

    fun <T : Any> generateSetToList(
        model: T,
        excludedFields: Collection<String> = setOf(),
        allowedNull: Boolean = false,
    ): List<SetTo<*>> = formatAsPropertyList(
        model = model,
        excludedFields = excludedFields,
        allowedNull = allowedNull,
    ).map { property ->
        SetTo(property, property.get(model))
    }
}
