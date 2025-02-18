import com.zmkn.service.LoggerService
import kotlin.test.Test

class LoggerServiceTest {
    data class User(
        val name: String,
        val age: Int,
    )

    @Test
    fun testDebug() {
        val user = User(
            name = "xiao zhang",
            age = 42
        )
        LoggerService.setLevel(LoggerService.Level.DEBUG)
        LoggerService.Builder()
            .build()
            .debug {
                user
            }
        LoggerService.getInstance().error(
            "aaaaaaaaaaaa",
            "bbbbbbbbbb",
            99999,
            8888888,
            77777777777,
            listOf(111111111, 2222222222)
        )
        LoggerService.getInstance().debug(
            Exception("qweasd123"),
            "bbbbbbbbbb",
            99999,
            mapOf("a" to 88888, "b" to Exception("xxxxxxxxxxxxx")),
            listOf("lllllll", 11111111111, Exception("yyyyyyyyyy")),
            user,
        )
        LoggerService.getInstance().error {
            "99999999999999999999"
        }
        LoggerService.getInstance().log(LoggerService.Level.ERROR, "8888888888888888")
        LoggerService.getInstance().log(LoggerService.Level.ERROR) {
            "7777777777777777777777"
        }
    }
}
