package com.zmkn.service

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder
import java.nio.ByteBuffer

class SerializationService {
    constructor() : this({})

    constructor(builderAction: JsonBuilder.() -> Unit) : this(Companion.json, builderAction)

    constructor(from: Json, builderAction: JsonBuilder.() -> Unit) {
        json = Json(from, builderAction)
    }

    val json: Json

    inline fun <reified T> read(binary: ByteBuffer): T {
        if (binary.hasArray()) {
            val arrayOffset = binary.arrayOffset()
            val bytes = binary.array()
            val jsonString = bytes.decodeToString(arrayOffset)
            return json.decodeFromString(jsonString)
        } else {
            throw Throwable("Serializer 工具的 read 方法执行失败，binary 缓冲器不是数组。")
        }
    }

    inline fun <reified T> serialize(data: T): ByteBuffer {
        val jsonString = json.encodeToString(data)
        val bytes = jsonString.toByteArray()
        return ByteBuffer.wrap(bytes)
    }

    inline fun <reified T> equals(
        data: T,
        binary: ByteBuffer,
    ): Boolean {
        val serialize = serialize(data)
        return binary == serialize
    }

    companion object {
        val json = Json {
            // 编码默认值
            encodeDefaults = true
            // 不显式处理null值
            explicitNulls = false
            // 忽略反序列化过程中遇到的未知键
            ignoreUnknownKeys = true
            // 不处理不符合标准 JSON 规范的数据
            isLenient = false
            // 在输出时美化打印JSON格式
            prettyPrint = true
            // decodeFromString 时不处理不符合预期类型的数值
            coerceInputValues = false
            // 允许使用别名
            useAlternativeNames = true
            // 不允许解析特殊浮点数
            allowSpecialFloatingPointValues = false
            // 不允许使用结构化的键名
            allowStructuredMapKeys = false
        }
    }
}
