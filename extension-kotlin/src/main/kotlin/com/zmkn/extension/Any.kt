package com.zmkn.extension

import com.google.protobuf.*
import com.zmkn.serialization.jackson.Jackson
import kotlin.reflect.KProperty1
import com.google.protobuf.Any as ProtobufAny

@Suppress("UNCHECKED_CAST")
fun <K, V> Any.toMap(): Map<K, V> {
    return Jackson.objectMapper.convertValue(this, Map::class.java) as Map<K, V>
}

fun Any?.toProtobufAny(): ProtobufAny {
    return when (this) {
        is String -> ProtobufAny.pack(StringValue.of(this))
        is Int -> ProtobufAny.pack(Int32Value.of(this))
        is Long -> ProtobufAny.pack(Int64Value.of(this))
        is Double -> ProtobufAny.pack(DoubleValue.of(this))
        is Float -> ProtobufAny.pack(FloatValue.of(this))
        is Boolean -> ProtobufAny.pack(BoolValue.of(this))
        is Char -> ProtobufAny.pack(Int32Value.of(code))
        is Short -> ProtobufAny.pack(Int32Value.of(toInt()))
        is Byte -> ProtobufAny.pack(Int32Value.of(toInt()))
        is ByteString -> ProtobufAny.pack(BytesValue.of(this))
        is ByteArray -> ProtobufAny.pack(BytesValue.of(ByteString.copyFrom(this)))
        null -> ProtobufAny.pack(Empty.newBuilder().build())
        else -> ProtobufAny.newBuilder().build()
    }
}

/**
 * 通过属性名获取对象属性值
 * @param propertyName 属性名称
 * @return 属性值，未找到时返回 null
 */
fun Any.getPropertyValue(propertyName: String): Any? {
    return (this::class.findProperty(propertyName) as? KProperty1<Any, *>)?.get(this)
}

/**
 * 通过属性名获取对象属性值（带类型转换）
 * @param propertyName 属性名称
 * @return 属性值，未找到或类型不匹配时返回 null
 */
@Suppress("UNCHECKED_CAST")
fun <T> Any.getPropertyValueAs(propertyName: String): T? {
    return getPropertyValue(propertyName) as? T
}
