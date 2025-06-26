import com.zmkn.service.SerializationService
import database.model.User
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.time.ExperimentalTime

class SerializationServiceText {
    private val _serializationService = SerializationService()

    @OptIn(ExperimentalTime::class)
    @Test
    fun testUser() = runBlocking {
        val user = User(
            accountId = "abcdefege",
            nickName = "kz",
            profilePictureUrl = "baidu.com",
            status = "DISABLED",
            phoneNumbers = emptyList(),
        )
        println(user)
        val str = _serializationService.json.encodeToString(user)
        println(str)
    }
}
