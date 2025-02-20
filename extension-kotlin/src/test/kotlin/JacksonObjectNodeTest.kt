import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.zmkn.extension.assign
import kotlin.test.Test

class JacksonObjectNodeTest {
    private val _objectMapper = jacksonObjectMapper()

    @Test
    fun testAssign() {
        println("testAssign---start")
        val jsonNodeMain = _objectMapper.readTree("""{ "a": "aaa111", "b": "bbb111" }""")
        println(jsonNodeMain)
        val jsonNode1 = _objectMapper.readTree("""{ "b": "bbb222", "c": [ "c1", 222 ], "d": [ "d1", 333, "444", "555", "666", "777" ], "e": "e111", "g": 888 }""")
        println(jsonNode1)
        val jsonNode2 = _objectMapper.readTree("""{ "c": [ 111, "c222", 3333 ], "d": [ 222, "d3333" ], "e": "e222", "f": 999 }""")
        println((jsonNodeMain as ObjectNode).assign(jsonNode1 as ObjectNode, jsonNode2 as ObjectNode))
        println("testAssign---end")
    }
}
