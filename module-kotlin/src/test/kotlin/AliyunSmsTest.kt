import com.zmkn.module.aliyunsms.AliyunSms
import com.zmkn.module.aliyunsms.model.Config
import com.zmkn.module.aliyunsms.model.SendSmsRequest
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Disabled
import kotlin.test.Test

class AliyunSmsTest {
    val aliyunSms = AliyunSms(
        Config(
            id = "",
            secret = "",
        )
    )

    @Test
    @Disabled
    fun testSent() = runBlocking {
        val response = aliyunSms.send(
            SendSmsRequest(
                phoneNumber = "16601190129",
                signName = "灵祇",
                templateCode = "",
                templateParam = mapOf("code" to "123456")
            )
        )
        println(response)
    }
}
