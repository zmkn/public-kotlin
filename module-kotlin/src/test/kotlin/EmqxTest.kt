import com.zmkn.module.emqx.EmqxApi
import com.zmkn.module.emqx.model.Config
import com.zmkn.module.emqx.model.PublishRequestBody
import kotlinx.coroutines.runBlocking

class EmqxTest {
    private val emqxApi = EmqxApi(
        Config(
            key = "",
            secret = "",
            baseUrl = "https://emqx.test.ailingqi.com:18084/api/v5",
        )
    )

    //    @Test
    fun testGetClients() = runBlocking {
        val response = emqxApi.getClients()
        println(response)
    }

    //    @Test
    fun testPublish() = runBlocking {
        val response = emqxApi.publish(
            PublishRequestBody(
                topic = "chat/user/1361021556018642944/inbox",
                payload = "data",
            )
        )
        println(response)
    }
}
