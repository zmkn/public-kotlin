import com.google.protobuf.EnumValue
import com.google.protobuf.Option
import com.google.protobuf.StringValue
import com.zmkn.extension.toAny
import com.zmkn.extension.toList
import com.zmkn.extension.toMap
import kotlin.test.Test
import com.google.protobuf.Any as GoogleProtobufAny

class AnyTest {
    data class User(val name: String, val age: Int)

    @Test
    fun testToMap() {
        println("testToMap---start")
        val user = User(name = "Alice", age = 29)
        val m = user.toMap()
        println(m)
        println("testToMap---end")
    }

    //    @Test
    fun testGoogleProtobufAnyToAny() {
        println("testGoogleProtobufAnyToAny")
        val stringValue = StringValue.newBuilder()
            .setValue("hello")
            .build()
        println(stringValue)
        val stringValueAny = GoogleProtobufAny.pack(stringValue)
        println(stringValueAny.toAny())
        val option = Option.newBuilder()
            .setName("名字")
            .setValue(stringValueAny)
            .build()
        println(option)
        val optionAny = GoogleProtobufAny.pack(option)
        println(optionAny.toAny())
        val option2 = Option.newBuilder()
            .setName("名字2")
            .setValue(stringValueAny)
            .build()
        val enumValue = EnumValue.newBuilder()
            .addOptions(option)
            .addOptions(option2)
            .build()
        println(enumValue)
        println(enumValue.toList())
    }
}
