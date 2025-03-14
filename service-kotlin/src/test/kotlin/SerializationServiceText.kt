import com.zmkn.service.LoggerService
import com.zmkn.service.SerializationService
import database.model.User
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class SerializationServiceText {
    private val _logger = LoggerService.Companion.getInstance()
    private val _serializationService = SerializationService()

    @Test
    fun testUser() = runBlocking {
        val user = User(
            accountId = "abcdefege",
            nickName = "kz",
            profilePictureUrl = "baidu.com",
            status = "DISABLED",
            phoneNumbers = emptyList(),
        )
        val str = _serializationService.json.encodeToString(user)
        _logger.error(str)
    }
}
