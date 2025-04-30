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
        val texts = listOf("韩立几经努力，结成金丹，期间偶得噬金虫。结识紫灵得金雷竹。此时谣言传出，称乱星海之掌控者“天星双圣”修炼走火入魔，乱星海乱象渐起。不久后又逢三百年一次的“虚天殿”开启，韩立有幸进入探宝，机缘巧合之下获得乱星海第一秘宝虚天鼎，为乱星海所有势力觊觎，乃传送至外星海，杀妖取丹，取丹炼药，服药炼气。悠悠数十载过，而后搜寻药材，不幸遭遇妖修，被胁迫帮助炼器，后于韩立精心设计下，暗算众妖修，灭杀毒蛟并获得逆天法宝风雷翅，而后为躲避妖修追杀，坐传送阵偷回内海。巧遇故友元瑶施展逆天大法，为其护法过程中遭遇六道传人，在快将其击败时遭遇百年一遇的绝灵之气，为鬼雾传送至阴魂之地，一番波折险死脱身后被传送至无边海，终回天南。")
        val options =
            SpeechSynthesisParamOptions(
                model = "cosyvoice-v2",
                texts = texts,
                voice = "longwan_v2",
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
