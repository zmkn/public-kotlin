import com.zmkn.util.JsonUtils
import org.junit.jupiter.api.Disabled
import kotlin.test.Test

class JsonUtilsTest {
    @Test
    @Disabled
    fun testIsLikelyJson() {
        println("testIsLikelyJson---start")
        val jsonString = "{}"
        val result = JsonUtils.isLikelyJson(jsonString)
        println(result)
        println("testIsLikelyJson---end")
    }

    @Test
    @Disabled
    fun testIsJsonValid() {
        println("testIsJsonValid---start")
        val jsonString = "{}"
        val result = JsonUtils.isJsonValid(jsonString)
        println(result)
        println("testIsJsonValid---end")
    }

    @Test
    @Disabled
    fun testIsObjectJson() {
        println("testIsObjectJson---start")
        val jsonString = "{}"
        val result = JsonUtils.isObjectJson(jsonString)
        println(result)
        println("testIsObjectJson---end")
    }

    @Test
    @Disabled
    fun testIsArrayJson() {
        println("testIsArrayJson---start")
        val jsonString = "[]"
        val result = JsonUtils.isArrayJson(jsonString)
        println(result)
        println("testIsArrayJson---end")
    }
}
