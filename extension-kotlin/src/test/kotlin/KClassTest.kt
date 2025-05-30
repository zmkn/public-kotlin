import com.zmkn.extension.findPropertyTypeAs
import org.junit.jupiter.api.Disabled
import kotlin.reflect.KClass
import kotlin.test.Test

class KClassTest {
    data class User(val name: String, val age: Int)

    @Test
    @Disabled
    fun testFindPropertyTypeAs() {
        println("testFindPropertyTypeAs---start")
        val user = User(name = "Alice", age = 29)
        val name = user::class.findPropertyTypeAs<User, KClass<*>>("name")
        println(name)
        println(name == String::class)
        println("testFindPropertyTypeAs---end")
    }
}
