import com.zmkn.module.aliyunllm.audio.Audio
import com.zmkn.module.aliyunllm.audio.Voice
import com.zmkn.module.aliyunllm.audio.model.SpeechSynthesisParamOptions
import com.zmkn.module.aliyunllm.audio.model.VoiceEnrollmentCreateOptions
import com.zmkn.module.aliyunllm.model.ApiOptions
import com.zmkn.util.FileUtils
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileOutputStream

class AliyunLlmAudioTest {
    private val _voice = Voice(
        apiKeys = listOf("123465", ""),
    )

    private val _audio = Audio(
        apiKeys = listOf("123465", ""),
        apiOptions = ApiOptions(
            connectionOptions = ApiOptions.ConnectionOptions(
                connectTimeout = 30,
                connectionPoolSize = 1000,
                maximumAsyncRequests = 1000,
                maximumAsyncRequestsPerHost = 1000,
            ),
        ),
    )

    val audioFile = File(FileUtils.getProjectRootDirectory("temp", "${Clock.System.now().epochSeconds}.mp3")).apply {
        parentFile.mkdirs()
    }

    @Test
    @Disabled
    fun testCreateStreamSpeechSynthesizer() = runBlocking {
        println("开始-testCreateStreamSpeechSynthesizer")
        val fos = FileOutputStream(audioFile)
        val texts = listOf("韩立出身贫苦，为光大门楣，童年参加了七玄门的考核，因身具灵根，可修炼《长春功》而被七玄门神秘的墨大夫收为弟子。期间韩立捡到逆天小瓶，可用于无限催熟植物。墨大夫本想待韩立修炼长春诀有小成后对其进行夺舍，结果失败，韩立也被其暗算下毒，必须找墨大夫家人以求解药从此得知修仙界存在。")
        val options =
            SpeechSynthesisParamOptions(
                model = "cosyvoice-v1",
                texts = texts,
                voice = "longcheng",
                pitchRate = 1.0f,
            )
        _audio.createStreamSpeechSynthesizer(options).catch {
            println("catch")
            println(it)
        }.collect {
            if (it.audios != null) {
                fos.write(it.audios)
            }
            if (it.usage != null) {
                println(it)
            }
        }
        fos.close()
        println("结束-testCreateStreamSpeechSynthesizer")
    }

    @Test
    @Disabled
    fun testCreateVoice() = runBlocking {
        println("开始-testCreateVoice")
        val responseVoice = _voice.createVoice(
            VoiceEnrollmentCreateOptions(
                model = "cosyvoice-v1",
                prefix = "aaa1",
                url = "http://hz.joyfulboy.cn/voice/001.mp3",
            )
        )
        println(responseVoice)
        println("结束-testCreateVoice")
    }

    @Test
//    @Disabled
    fun testQueryAll() = runBlocking {
        println("开始-testQueryAll")
        val list = _voice.queryAll("aaa1", 0, 10)
        println(list)
        println("结束-testQueryAll")
    }
}
