import com.zmkn.module.aliyunoss.AliyunOss
import com.zmkn.module.aliyunoss.model.Config
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Disabled
import kotlin.test.Test

class AliyunOssTest {
    private val _filePath = "a123.jpg"
    private val _aliyunOss = AliyunOss(
        Config(
            id = "",
            secret = "",
            bucket = "api-test-lingqi",
            region = "cn-huhehaote",
            endpoint = "oss-cn-huhehaote.aliyuncs.com",
        )
    )

    @Test
    @Disabled
    fun testUpload() = runBlocking {
        this::class.java.getResourceAsStream("/1.jpg").use { stream ->
            if (stream != null) {
                _aliyunOss.upload(_filePath, stream)
            }
        }
    }

    @Test
    @Disabled
    fun testFileExists() = runBlocking {
        val result = _aliyunOss.fileExists(_filePath)
        println(result)
    }

    @Test
    @Disabled
    @OptIn(ExperimentalStdlibApi::class)
    fun testDownload() = runBlocking {
        val result = _aliyunOss.download(_filePath)
        println(result.readAllBytes().toHexString())
    }

    @Test
    @Disabled
    fun testDelete() = runBlocking {
        val result = _aliyunOss.delete(_filePath)
        println(result)
    }
}
