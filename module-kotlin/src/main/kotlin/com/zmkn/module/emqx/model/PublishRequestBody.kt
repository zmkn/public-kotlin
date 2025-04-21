package com.zmkn.module.emqx.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.zmkn.module.emqx.model.PublishRequestBody.PayloadEncoding.PLAIN
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class PublishRequestBody(
    // 主题
    val topic: String,
    // MQTT 消息体
    val payload: String,
    // MQTT 消息体的编码方式
    @Serializable(with = PayloadEncoding.PayloadEncodingSerializer::class)
    @param:JsonSerialize(using = PayloadEncoding.PayloadEncodingJacksonSerializer::class)
    @param:JsonDeserialize(using = PayloadEncoding.PayloadEncodingJacksonDeserializer::class)
    @SerialName("payload_encoding")
    @param:JsonProperty("payload_encoding")
    val payloadEncoding: PayloadEncoding = PLAIN,
    // QoS
    val qos: Int = 0,
    // 是否是保留消息
    val retain: Boolean = false,
    val properties: Properties? = null,
) {
    @Serializable
    data class Properties(
        // Payload 格式，0 (0x00) 字节表示是未指定格式的数据，相当于没有发送 Payload 格式指示；1 (0x01) 字节表示 Payload 是 UTF-8 编码的字符数据，Payload 中的 UTF-8 数据必须是按照 Unicode 的规范和 RFC 3629 的标准要求进行编码的。
        @SerialName("payload_format_indicator")
        @param:JsonProperty("payload_format_indicator")
        val payloadFormatIndicator: Int = 0,
        // 消息过期间隔的标识符。如果消息过期间隔已过，而服务器未能开始向匹配的订阅者转发消息，则必须删除该订阅者的消息副本。
        @SerialName("message_expiry_interval")
        @param:JsonProperty("message_expiry_interval")
        val messageExpiryInterval: Int = 0,
        // 响应主题的标识符。响应主题必须是 UTF-8 编码的，且不得包含通配符字符。
        @SerialName("response_topic")
        @param:JsonProperty("response_topic")
        val responseTopic: String = "",
        // 关联数据的标识符，服务器必须将关联数据原封不动地发送给接收应用消息的所有的订阅者。
        @SerialName("correlation_data")
        @param:JsonProperty("correlation_data")
        val correlationData: String = "",
        // 消息内容类型必须是 UTF-8 编码的字符串。
        @Serializable(with = ContentType.ContentTypeSerializer::class)
        @param:JsonSerialize(using = ContentType.ContentTypeJacksonSerializer::class)
        @param:JsonDeserialize(using = ContentType.ContentTypeJacksonDeserializer::class)
        @SerialName("content_type")
        @param:JsonProperty("content_type")
        val contentType: ContentType = ContentType.TEXT_PLAIN,
        // 指定 MQTT 消息的 User Property 键值对。注意，如果出现重复的键，只有最后一个会保留。
        @SerialName("user_properties")
        @param:JsonProperty("user_properties")
        val userProperties: Map<String, String> = emptyMap(),
    ) {
        enum class ContentType(val value: String) {
            TEXT_PLAIN("text/plain"),
            APPLICATION_JSON("application/json"),
            APPLICATION_OCTET_STREAM("application/octet-stream");

            override fun toString(): String {
                return value
            }

            object ContentTypeSerializer : KSerializer<ContentType> {
                override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("com.zmkn.module.emqx.model.PublishRequestBody.Properties.ContentType", PrimitiveKind.STRING)

                override fun serialize(encoder: Encoder, value: ContentType) = encoder.encodeString(value.value)

                override fun deserialize(decoder: Decoder): ContentType = ContentType.fromValue(decoder.decodeString())
            }

            class ContentTypeJacksonSerializer : JsonSerializer<ContentType>() {
                override fun serialize(value: ContentType, gen: JsonGenerator, serializers: SerializerProvider) = gen.writeString(value.value)
            }

            class ContentTypeJacksonDeserializer : JsonDeserializer<ContentType>() {
                override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ContentType = ContentType.fromValue(p.valueAsString)
            }

            companion object {
                fun fromValue(value: String): ContentType {
                    return when (value) {
                        TEXT_PLAIN.value -> TEXT_PLAIN
                        APPLICATION_JSON.value -> APPLICATION_JSON
                        APPLICATION_OCTET_STREAM.value -> APPLICATION_OCTET_STREAM
                        else -> throw IllegalArgumentException("ContentType value is not allowed.")
                    }
                }
            }
        }
    }

    enum class PayloadEncoding(val value: String) {
        PLAIN("plain"),
        BASE64("base64");

        override fun toString(): String {
            return value
        }

        object PayloadEncodingSerializer : KSerializer<PayloadEncoding> {
            override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("com.zmkn.module.emqx.model.PublishRequestBody.PayloadEncoding", PrimitiveKind.STRING)

            override fun serialize(encoder: Encoder, value: PayloadEncoding) = encoder.encodeString(value.value)

            override fun deserialize(decoder: Decoder): PayloadEncoding = PayloadEncoding.fromValue(decoder.decodeString())
        }

        class PayloadEncodingJacksonSerializer : JsonSerializer<PayloadEncoding>() {
            override fun serialize(value: PayloadEncoding, gen: JsonGenerator, serializers: SerializerProvider) = gen.writeString(value.value)
        }

        class PayloadEncodingJacksonDeserializer : JsonDeserializer<PayloadEncoding>() {
            override fun deserialize(p: JsonParser, ctxt: DeserializationContext): PayloadEncoding = PayloadEncoding.fromValue(p.valueAsString)
        }

        companion object {
            fun fromValue(value: String): PayloadEncoding {
                return when (value) {
                    PLAIN.value -> PLAIN
                    BASE64.value -> BASE64
                    else -> throw IllegalArgumentException("PayloadEncoding value is not allowed.")
                }
            }
        }
    }
}
