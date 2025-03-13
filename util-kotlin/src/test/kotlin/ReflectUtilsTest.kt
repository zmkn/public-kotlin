import com.zmkn.util.ReflectUtils
import org.junit.jupiter.api.Test

class ReflectUtilsTest {
    data class User(
        val id: Int,
        val name: String,
        val email: String,
    )

    @Test
    fun testGetKClass() {
        println("testGetKClass------start")
        val kClass = ReflectUtils.getKClass(User::class, User::name.name)
        println(kClass)
        println(kClass.java)
        println("testGetKClass------end")
    }
}
