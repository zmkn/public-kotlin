import com.zmkn.extension.toDuration
import kotlin.test.Test

class StringTest {
    @Test
    fun testToDuration() {
        println("testToDuration---start")
        val duration = "100.0001s".toDuration()
        println(duration)
        println("testToDuration---end")
    }
}
