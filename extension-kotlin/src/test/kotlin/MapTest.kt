import com.zmkn.extension.toJson
import kotlin.test.Test

class MapTest {
    @Test
    fun testMap() {
        val map = mapOf("a" to "aaaa", "b" to "111", "3" to mapOf("c" to "c1"))
        println(map)
        println(map.toJson())
    }
}
