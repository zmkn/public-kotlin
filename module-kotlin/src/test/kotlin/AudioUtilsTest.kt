import com.zmkn.module.aliyunllm.audio.util.AudioUtils
import org.junit.jupiter.api.Disabled
import kotlin.test.Test

class AudioUtilsTest {
    @Test
    @Disabled
    fun testSplitText() {
        println("testSplitText---start")
        val text = "韩立出身贫苦，为光大门楣，童年参加了七玄门的考核，因身具灵根，可修炼《长春功》而被七玄门神秘的墨大夫收为弟子。期间韩立捡到逆天小瓶，可用于无限催熟植物。墨大夫本想待韩立修炼长春诀有小成后对其进行夺舍，结果失败，韩立也被其暗算下毒，必须找墨大夫家人以求解药从此得知修仙界存在。"
        val texts = AudioUtils.splitSpeechSynthesizerText(text)
        println(texts)
        println("testSplitText---end")
    }
}
