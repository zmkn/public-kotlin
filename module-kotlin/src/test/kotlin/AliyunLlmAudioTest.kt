import com.zmkn.module.aliyunllm.audio.Audio
import com.zmkn.module.aliyunllm.audio.model.SpeechSynthesisParamOptions
import com.zmkn.util.FileUtils
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileOutputStream

class AliyunLlmAudioTest {
    private val _audio = Audio(listOf("123465", ""))

    val audioFile = File(FileUtils.getProjectRootDirectory("temp", "${Clock.System.now().epochSeconds}.mp3")).apply {
        parentFile.mkdirs()
    }

    @Test
//    @Disabled
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
}
