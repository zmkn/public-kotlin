import com.zmkn.module.aliyunllm.audio.Audio
import com.zmkn.module.aliyunllm.audio.Voice
import com.zmkn.module.aliyunllm.audio.model.CreateVoiceOptions
import com.zmkn.module.aliyunllm.audio.model.EnrollVoiceOptions
import com.zmkn.module.aliyunllm.audio.model.SpeechSynthesisParamOptions
import com.zmkn.module.aliyunllm.audio.model.SpeechSynthesisParamOptions.TextType
import com.zmkn.module.aliyunllm.model.ApiOptions
import com.zmkn.util.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileOutputStream
import kotlin.time.Clock

class AliyunLlmAudioTest {
    private val _voice: Voice by lazy {
        Voice(
            apiKeys = listOf("123", ""),
        )
    }

    private val _audio: Audio by lazy {
        Audio(
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
    }

    val audioFile = File(FileUtils.getProjectRootDirectory("temp", "${Clock.System.now().epochSeconds}.mp3")).apply {
        parentFile.mkdirs()
    }

    @Disabled
    @Test
    fun testCreateStreamSpeechSynthesizer() = runBlocking {
        println("开始-testCreateStreamSpeechSynthesizer")
        val fos = FileOutputStream(audioFile)
        val texts = listOf("<speak rate=\"0.7\" pitch=\"1.0\" volume=\"100\" bgm=\"http://nls.alicdn.com/bgm/2.wav\" backgroundMusicVolume=\"30\">星阳帝国，遂州城。漫天乌云，透着一股沉闷无比的气息。</speak>", "<speak rate=\"0.7\" pitch=\"1.0\" volume=\"100\" bgm=\"http://nls.alicdn.com/bgm/2.wav\" backgroundMusicVolume=\"30\">“黄泉路长无客栈，看好脚下，上路了！”</speak>")
        val options =
            SpeechSynthesisParamOptions(
                model = "cosyvoice-v3-flash",
                texts = texts,
                voice = "longanyang",
                pitchRate = 1.0f,
                textType = TextType.SSML,
                enableWordTimestamp = true,
                enablePhonemeTimestamp = true,
            )
        _audio.createStreamSpeechSynthesizer(options).catch {
            println("catch")
            println(it)
        }.collect {
            if (it.audios != null) {
                withContext(Dispatchers.IO) {
                    fos.write(it.audios)
                }
            }
            if (it.usage != null) {
                println(it)
            }
        }
        fos.close()
        println("结束-testCreateStreamSpeechSynthesizer")
    }

    @Disabled
    @Test
    fun testCreateVoice() = runBlocking {
        println("开始-testCreateVoice")
        val responseVoice = _voice.createVoice(
            CreateVoiceOptions(
                input = CreateVoiceOptions.Input(
                    targetModel = "cosyvoice-v3.5-plus",
                    prefix = "lingqi",
                    voicePrompt = "沉稳的中年男性播音员，音色低沉浑厚，富有磁性，语速平稳，吐字清晰，适合用于新闻播报或纪录片解说。",
                    previewText = "各位听众朋友，大家好，欢迎收听晚间新闻。",
                ),
            ),
        )
        println(responseVoice)
        println(responseVoice.output.voiceId)
        println("结束-testCreateVoice")
    }

    @Disabled
    @Test
    fun testEnrollVoice() = runBlocking {
        println("开始-testEnrollVoice")
        val responseVoice = _voice.enrollVoice(
            EnrollVoiceOptions(
                model = "cosyvoice-v3.5-flash",
                prefix = "lingqi",
                url = "https://oss.test.ailingqi.com/voice/user/67d11287d0f1c354bbad4c1e/wav/S9tc3fNoQ2NwZWAW.wav",
                languageHints = null
            )
        )
        println(responseVoice)
        println("结束-testEnrollVoice")
    }

    @Disabled
    @Test
    fun testQueryAllEnrolledVoices() = runBlocking {
        println("开始-testQueryAllEnrolledVoices")
        val list = _voice.queryAllEnrolledVoices("lingqi", 0, 10)
        list.forEach {
            println(it.voiceId)
        }
        println(list)
        println("结束-testQueryAllEnrolledVoices")
    }

    @Test
    @Disabled
    fun testQueryEnrolledVoice() = runBlocking {
        println("开始-testQueryEnrolledVoice")
        val voice = _voice.queryEnrolledVoice("cosyvoice-v1-aaa1-3aff2904a86c400bbd4e77eca17b7da7")
        println(voice)
        println("结束-testQueryEnrolledVoice")
    }

    @Test
    @Disabled
    fun testUpdateEnrolledVoice() = runBlocking {
        println("开始-testUpdateEnrolledVoice")
        val result = _voice.updateEnrolledVoice(
            id = "cosyvoice-v1-aaa1-3aff2904a86c400bbd4e77eca17b7da7",
            url = "http://hz.joyfulboy.cn/voice/001.mp3",
        )
        println(result)
        println("结束-testUpdateEnrolledVoice")
    }

    @Disabled
    @Test
    fun testDeleteEnrolledVoice() = runBlocking {
        println("开始-testDeleteEnrolledVoice")
        val result = _voice.deleteEnrolledVoice("cosyvoice-v3.5-flash-lingqi-a92f45844ed24edf884cd5288dfb35be")
        println(result)
        println("结束-testDeleteEnrolledVoice")
    }
}
