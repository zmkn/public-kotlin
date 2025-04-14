import com.zmkn.util.JsonUtils
import kotlin.test.Test

class JsonUtilsTest {
    @Test
    fun testIsLikelyJson() {
        println("testIsLikelyJson---start")
        val jsonString = "{}"
        val result = JsonUtils.isLikelyJson(jsonString)
        println(result)
        println("testIsLikelyJson---end")
    }

    @Test
    fun testIsJsonValid() {
        println("testIsJsonValid---start")
        val jsonString = "{}"
        val result = JsonUtils.isJsonValid(jsonString)
        println(result)
        println("testIsJsonValid---end")
    }

    @Test
    fun testIsObjectJson() {
        println("testIsObjectJson---start")
        val jsonString = "{}"
        val result = JsonUtils.isObjectJson(jsonString)
        println(result)
        println("testIsObjectJson---end")
    }

    @Test
    fun testIsArrayJson() {
        println("testIsArrayJson---start")
        val jsonString = "[]"
        val result = JsonUtils.isArrayJson(jsonString)
        println(result)
        println("testIsArrayJson---end")
    }
}
