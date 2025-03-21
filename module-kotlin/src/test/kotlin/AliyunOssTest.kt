import com.aliyun.oss.event.ProgressEventType
import com.zmkn.module.aliyunoss.AliyunOss
import com.zmkn.module.aliyunoss.model.Config
import com.zmkn.util.FileUtils
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Disabled
import java.io.File
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
    fun testUpload(): Unit = runBlocking {
        val filePath = "${FileUtils.getProjectRootDirectory()}\\src\\test\\resources\\1.jpg"
        println(filePath)
        var totalBytes = -1L
        var bytesWritten = 0L
        var succeed = false
        _aliyunOss.upload(_filePath, File(filePath)) {
            when (it.eventType) {
                ProgressEventType.REQUEST_CONTENT_LENGTH_EVENT -> {
                    totalBytes = it.bytes
                    println("总共 $totalBytes 字节将被上传到OSS")
                }

                ProgressEventType.RESPONSE_CONTENT_LENGTH_EVENT -> {}
                ProgressEventType.REQUEST_BYTE_TRANSFER_EVENT -> {
                    bytesWritten += it.bytes
                    if (totalBytes != -1L) {
                        val percent = bytesWritten * 100 / totalBytes
                        println("已上传 ${it.bytes} 字节, 上传进度: $percent%($bytesWritten/$totalBytes)")
                    } else {
                        println("已上传 ${it.bytes} 字节, 上传进度: 未知($bytesWritten/...)")
                    }
                }

                ProgressEventType.RESPONSE_BYTE_TRANSFER_EVENT -> {}
                ProgressEventType.TRANSFER_PREPARING_EVENT -> {}
                ProgressEventType.TRANSFER_STARTED_EVENT -> {
                    println("开始上传")
                }

                ProgressEventType.TRANSFER_COMPLETED_EVENT -> {
                    succeed = true
                    println("上传成功, 共传输 $bytesWritten 字节")
                }

                ProgressEventType.TRANSFER_FAILED_EVENT -> {
                    println("上传失败, 已传输 $bytesWritten 字节")
                }

                ProgressEventType.TRANSFER_CANCELED_EVENT -> {}
                ProgressEventType.TRANSFER_PART_STARTED_EVENT -> {}
                ProgressEventType.TRANSFER_PART_COMPLETED_EVENT -> {}
                ProgressEventType.TRANSFER_PART_FAILED_EVENT -> {}
                ProgressEventType.SELECT_STARTED_EVENT -> {}
                ProgressEventType.SELECT_SCAN_EVENT -> {}
                ProgressEventType.SELECT_COMPLETED_EVENT -> {}
                ProgressEventType.SELECT_FAILED_EVENT -> {}
            }
        }
    }

    @Test
    @Disabled
    fun testUpload2() = runBlocking {
        this::class.java.getResourceAsStream("/1.jpg").use { stream ->
            if (stream != null) {
                var totalBytes = -1L
                var bytesWritten = 0L
                var succeed = false
                _aliyunOss.upload(_filePath, stream) {
                    when (it.eventType) {
                        ProgressEventType.REQUEST_CONTENT_LENGTH_EVENT -> {
                            totalBytes = it.bytes
                            println("总共 $totalBytes 字节将被上传到OSS")
                        }

                        ProgressEventType.RESPONSE_CONTENT_LENGTH_EVENT -> {}
                        ProgressEventType.REQUEST_BYTE_TRANSFER_EVENT -> {
                            bytesWritten += it.bytes
                            if (totalBytes != -1L) {
                                val percent = bytesWritten * 100 / totalBytes
                                println("已上传 ${it.bytes} 字节, 上传进度: $percent%($bytesWritten/$totalBytes)")
                            } else {
                                println("已上传 ${it.bytes} 字节, 上传进度: 未知($bytesWritten/...)")
                            }
                        }

                        ProgressEventType.RESPONSE_BYTE_TRANSFER_EVENT -> {}
                        ProgressEventType.TRANSFER_PREPARING_EVENT -> {}
                        ProgressEventType.TRANSFER_STARTED_EVENT -> {
                            println("开始上传")
                        }

                        ProgressEventType.TRANSFER_COMPLETED_EVENT -> {
                            succeed = true
                            println("上传成功, 共传输 $bytesWritten 字节")
                        }

                        ProgressEventType.TRANSFER_FAILED_EVENT -> {
                            println("上传失败, 已传输 $bytesWritten 字节")
                        }

                        ProgressEventType.TRANSFER_CANCELED_EVENT -> {}
                        ProgressEventType.TRANSFER_PART_STARTED_EVENT -> {}
                        ProgressEventType.TRANSFER_PART_COMPLETED_EVENT -> {}
                        ProgressEventType.TRANSFER_PART_FAILED_EVENT -> {}
                        ProgressEventType.SELECT_STARTED_EVENT -> {}
                        ProgressEventType.SELECT_SCAN_EVENT -> {}
                        ProgressEventType.SELECT_COMPLETED_EVENT -> {}
                        ProgressEventType.SELECT_FAILED_EVENT -> {}
                    }
                }
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
