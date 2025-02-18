package com.zmkn.service

import com.google.protobuf.Message
import com.google.protobuf.MessageOrBuilder
import com.google.protobuf.util.JsonFormat
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass
import kotlin.reflect.full.starProjectedType

class ProtobufService(
    private val json: Json = SerializationService.json,
    private val parser: JsonFormat.Parser = JsonFormat.parser(),
    private val printer: JsonFormat.Printer = JsonFormat.printer()
) {
    fun encodeToString(message: MessageOrBuilder): String {
        return printer.print(message)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Message> decodeFromString(builder: Message.Builder, json: String): T {
        parser.merge(json, builder)
        return builder.build() as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> encode(schema: KClass<T>, message: MessageOrBuilder): T {
        val jsonString = encodeToString(message)
        return json.decodeFromString(json.serializersModule.serializer(schema.starProjectedType), jsonString) as T
    }

    fun <T : Message> decode(schema: KClass<*>, value: Any, builder: Message.Builder): T {
        val jsonString = json.encodeToString(json.serializersModule.serializer(schema.starProjectedType), value)
        return decodeFromString(builder, jsonString)
    }

    companion object {
        private val _instance by lazy { ProtobufService() }

        fun getInstance(): ProtobufService {
            return _instance
        }

        fun encodeToString(message: MessageOrBuilder): String = _instance.encodeToString(message)

        fun <T : Message> decodeFromString(builder: Message.Builder, json: String): T = _instance.decodeFromString(builder, json)

        fun <T : Any> encode(schema: KClass<T>, message: MessageOrBuilder): T = _instance.encode(schema, message)

        fun <T : Message> decode(schema: KClass<*>, value: Any, builder: Message.Builder): T = _instance.decode(schema, value, builder)
    }
}
