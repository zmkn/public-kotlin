import com.zmkn.extension.toJson
import org.junit.jupiter.api.Disabled
import kotlin.test.Test

class MapTest {
    @Test
    @Disabled
    fun testMap() {
        val map = mapOf("a" to "aaaa", "b" to "111", "3" to mapOf("c" to "c1"))
        println(map)
        println(map.toJson())
    }
}
