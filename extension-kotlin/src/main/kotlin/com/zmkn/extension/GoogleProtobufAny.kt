package com.zmkn.extension

import com.google.protobuf.*
import kotlin.Any
import com.google.protobuf.Any as GoogleProtobufAny

fun GoogleProtobufAny.toAny(): Any? {
    return when {
        this.`is`(StringValue::class.java) -> this.unpack(StringValue::class.java).value
        this.`is`(Int32Value::class.java) -> this.unpack(Int32Value::class.java).value
        this.`is`(UInt32Value::class.java) -> this.unpack(UInt32Value::class.java).value
        this.`is`(Int64Value::class.java) -> this.unpack(Int64Value::class.java).value
        this.`is`(UInt64Value::class.java) -> this.unpack(UInt64Value::class.java).value
        this.`is`(DoubleValue::class.java) -> this.unpack(DoubleValue::class.java).value
        this.`is`(FloatValue::class.java) -> this.unpack(FloatValue::class.java).value
        this.`is`(BoolValue::class.java) -> this.unpack(BoolValue::class.java).value
        this.`is`(BytesValue::class.java) -> this.unpack(BytesValue::class.java).value
        this.`is`(Value::class.java) -> this.unpack(Value::class.java).toAny()
        this.`is`(Option::class.java) -> this.unpack(Option::class.java).toMap()
        this.`is`(Struct::class.java) -> this.unpack(Struct::class.java).toMap()
        this.`is`(ListValue::class.java) -> this.unpack(ListValue::class.java).toList()
        this.`is`(EnumValue::class.java) -> this.unpack(EnumValue::class.java).toList()
        this.equals(NullValue.NULL_VALUE) -> null
        this.equals(NullValue.UNRECOGNIZED) -> null
        else -> null
    }
}
