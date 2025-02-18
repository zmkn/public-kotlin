import com.zmkn.module.alisms.AliSms
import com.zmkn.module.alisms.model.Config
import com.zmkn.module.alisms.model.SendSmsRequest
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Disabled
import kotlin.test.Test

class AliSms {
    val aliSms = AliSms(
        Config(
            id = "",
            secret = "",
        )
    )

    @Test
    @Disabled
    fun testSent() = runBlocking {
        val response = aliSms.send(
            SendSmsRequest(
                mobileNumber = "16601190129",
                signName = "灵祇",
                templateCode = "",
                templateParam = mapOf("code" to "123456")
            )
        )
        println(response)
    }
}
