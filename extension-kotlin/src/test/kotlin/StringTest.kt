import com.zmkn.extension.toDuration
import org.junit.jupiter.api.Disabled
import kotlin.test.Test

class StringTest {
    @Test
    @Disabled
    fun testToDuration() {
        println("testToDuration---start")
        val duration = "100.0001s".toDuration()
        println(duration)
        println("testToDuration---end")
    }
}
