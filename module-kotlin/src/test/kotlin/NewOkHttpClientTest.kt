import com.zmkn.module.okhttp.NewOkHttpClient
import com.zmkn.module.okhttp.interfaces.InterceptRequestListener
import com.zmkn.module.okhttp.listener.BaseUrlInterceptRequestListener
import com.zmkn.module.okhttp.listener.NacosNameResolveInterceptRequestListener
import com.zmkn.module.okhttp.util.OkHttpUtils
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.jupiter.api.Disabled
import kotlin.test.Test

class NewOkHttpClientTest {
    private fun createOkHttpClient(
        okHttpClient: OkHttpClient,
        baseUrl: String,
    ): OkHttpClient {
        val interceptor = OkHttpUtils.createInterceptor(
            interceptRequestListeners = mutableListOf<InterceptRequestListener>().apply {
                add(
                    NacosNameResolveInterceptRequestListener(
                        scheme = "http",
                    )
                )
                add(BaseUrlInterceptRequestListener(baseUrl))
            },
        )
        return okHttpClient.newBuilder()
            .addInterceptor(interceptor)
            .build()
    }

    @Test
    @Disabled
    fun test(): Unit = runBlocking {
        val newOkHttpClient = NewOkHttpClient(
            baseUrl = "nacos://ddd.com"
        )
        newOkHttpClient.okHttpClient = createOkHttpClient(newOkHttpClient.okHttpClient, "nacos://ddd.com")

        newOkHttpClient.get("/abc")
    }
}
