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
                model = "cosyvoice-v2",
                prefix = "lingqi",
                url = "http://hz.joyfulboy.cn/voice/001.mp3",
            )
        )
        println(responseVoice)
        println("结束-testCreateVoice")
    }

    @Test
    @Disabled
    fun testQueryAllVoices() = runBlocking {
        println("开始-testQueryAllVoices")
        val list = _voice.queryAllVoices("lingqi", 0, 10)
        println(list)
        println("结束-testQueryAllVoices")
    }

    @Test
    @Disabled
    fun testQueryVoice() = runBlocking {
        println("开始-testQueryVoice")
        val voice = _voice.queryVoice("cosyvoice-v1-aaa1-3aff2904a86c400bbd4e77eca17b7da7")
        println(voice)
        println("结束-testQueryVoice")
    }

    @Test
    @Disabled
    fun testUpdateVoice() = runBlocking {
        println("开始-testUpdateVoice")
        val result = _voice.updateVoice(
            id = "cosyvoice-v1-aaa1-3aff2904a86c400bbd4e77eca17b7da7",
            url = "http://hz.joyfulboy.cn/voice/001.mp3",
        )
        println(result)
        println("结束-testUpdateVoice")
    }

    @Test
    @Disabled
    fun testDeleteVoice() = runBlocking {
        println("开始-testDeleteVoice")
        val result = _voice.deleteVoice("cosyvoice-v1-aaa1-ce39a5e71a104ac1a8572ff9baed2f6b")
        println(result)
        println("结束-testDeleteVoice")
    }
}
