import com.zmkn.service.YamlService
import java.io.File
import kotlin.test.Test

private data class User(
    val name: String? = null,
    val age: Int? = null
)

class YamlServiceTest {
    @Test
    fun testRead() {
        println("testRead")
        val yamlService = YamlService()
        javaClass.getResource("/user1.yaml")?.apply {
            val userFile1 = File(toURI())
            val userFile2 = File(this@YamlServiceTest.javaClass.getResource("/user2.yaml")!!.toURI())
            val yaml = yamlService.read(User::class.java, userFile1.absolutePath, userFile2.absolutePath)
            println(yaml)
        }
        javaClass.getResource("/list1.yaml")?.apply {
            println("list")
            val listFile1 = File(toURI())
            val listFile2 = File(this@YamlServiceTest.javaClass.getResource("/list2.yaml")!!.toURI())
            yamlService.readList(User::class.java, listFile1.absolutePath, listFile2.absolutePath).forEach {
                println(it)
            }
        }
        val mergeMap = yamlService.merge(mapOf("a" to "a1", "b" to "b1", "c" to "c1"), mapOf("a" to "a2", "c" to "c2"), mapOf("b" to mapOf("bb" to "bb1"), "d" to "d2", "e" to mapOf("ee" to "ee1")))
        println(mergeMap)
        val mergeList = yamlService.merge(listOf("a", "b", "c"), listOf("b", mapOf("aa" to "aa1", "bb" to "bb1"), "e"), listOf("c"))
        println(mergeList)
    }

    @Test
    fun testWrite() {
        println("testWrite")
        val user = User(name = "名字", age = 1)
        val yamlService = YamlService()
        javaClass.getResource("/multiple.yaml")?.apply {
            val file = File(toURI())
//            yamlService.write(file, user)
//            yamlService.writeList(file, setOf("aaa", "bbb", "ccc"))
            yamlService.writeAll(file, listOf(listOf(user), listOf(mapOf("a" to "a1", "b" to "b1")), setOf("ccc", "ddd"), mapOf("aa" to "aa1", "bb" to "bb1")).iterator())
        }
    }
}
