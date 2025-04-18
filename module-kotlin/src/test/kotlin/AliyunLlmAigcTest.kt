import com.alibaba.dashscope.utils.JsonUtils
import com.zmkn.module.aliyunllm.aigc.Aigc
import com.zmkn.module.aliyunllm.aigc.enumeration.MessageRole
import com.zmkn.module.aliyunllm.aigc.enumeration.ResponseMessageChoiceFinishReason
import com.zmkn.module.aliyunllm.aigc.extension.toGenerationParamOptionsMessage
import com.zmkn.module.aliyunllm.aigc.model.*
import com.zmkn.module.aliyunllm.aigc.model.MultiModalConversationParamOptions.Message
import com.zmkn.module.aliyunllm.aigc.util.AigcUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class GetWeatherTool(
    private val location: String
) {
    fun call(): String = location + "今天是晴天"
}

class GetTimeTool {
    fun call(): String {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val currentTime = "当前时间：" + now.format(formatter) + "。"
        return currentTime
    }
}

class AliyunLlmAigcTest {
    private val _aigc = Aigc(listOf("", ""))

    private suspend fun createStreamMessageAndTools(messages: List<GenerationParamOptions.Message>, tools: List<GenerationParamOptions.Tool>? = null): Flow<ResponseMessage> {
        println("开始-createStreamMessageAndTools")
        println(messages)
        val options =
            GenerationParamOptions(
                model = "qwq-plus",
                messages = messages,
                tools = tools,
                enableSearch = false,
                searchOptions = GenerationParamOptions.SearchOptions(
                    enableSource = true,
                    forcedSearch = true,
                    searchStrategy = GenerationParamOptions.SearchOptions.SearchStrategy.STANDARD,
                )
            )
        return _aigc.createStreamMessage(options).transform { result ->
            val output = result.output
            val choice = output.choices[0]
            val message = choice.message
            val toolCalls = message.toolCalls
            if (toolCalls.isNullOrEmpty()) {
                emit(result)
            } else {
                if (choice.finishReason == ResponseMessageChoiceFinishReason.TOOL_CALLS) {
                    val toolCall = toolCalls[0]
                    val newMessages = messages.toMutableList()
                    newMessages.add(message.toGenerationParamOptionsMessage())
                    if (toolCall.function.name == "get_current_weather") {
                        val getWhetherFunction = JsonUtils.fromJson(toolCall.function.arguments, GetWeatherTool::class.java)
                        val whether = getWhetherFunction.call()
                        println(whether)
                        newMessages.add(
                            GenerationParamOptions.Message(
                                role = MessageRole.TOOL,
                                content = whether,
                                toolCallId = toolCall.id,
                            )
                        )
                    } else if (toolCall.function.name == "get_current_time") {
                        val getTimeFunction = JsonUtils.fromJson(toolCall.function.arguments, GetTimeTool::class.java)
                        val time = getTimeFunction.call()
                        println(time)
                        newMessages.add(
                            GenerationParamOptions.Message(
                                role = MessageRole.TOOL,
                                content = time,
                                toolCallId = toolCall.id,
                            )
                        )
                    }
                    println(newMessages)
                    emitAll(createStreamMessageAndTools(newMessages))
                }
            }
        }
    }

    private fun createStreamMultiModalMessage(messages: List<Message>): Flow<MultiModalResponseMessage> {
        println("开始-createStreamMultiModalMessage")
        println(messages)
        val options =
            MultiModalConversationParamOptions(
                model = "qwen-omni-turbo",
                messages = messages,
                modalities = listOf(MultiModalConversationParamOptions.Modality.TEXT),
                audio = MultiModalConversationParamOptions.AudioParameters(
                    voice = MultiModalConversationParamOptions.AudioParameters.Voice.CHERRY
                ),
            )
        return _aigc.createStreamMultiModalMessage(options)
    }

    @Test
    @Disabled
    fun testCreateStreamMessage() = runBlocking {
        createStreamMessageAndTools(
            listOf(
                GenerationParamOptions.Message(
                    role = MessageRole.SYSTEM,
                    content = "你是灵祇，一个全能的AI助手。请简短的回答。"
                ),
                GenerationParamOptions.Message(
                    role = MessageRole.USER,
                    content = "请用最少的话回答。kotlin开发中，如何收集一个 flow"
                )
            )
        ).collect {
            println(it.requestId)
            println(it.usage)
            println(it.output)
            println(JsonUtils.toJson(it))
        }
    }

    @Test
    @Disabled
    fun testCreateStreamMessageAndTools() = runBlocking {
        createStreamMessageAndTools(
            listOf(
                GenerationParamOptions.Message(
                    role = MessageRole.SYSTEM,
                    content = "你是灵祇，一个全能的AI助手。"
                ),
                GenerationParamOptions.Message(
                    role = MessageRole.USER,
                    content = "北京天气怎么样"
                )
            ),
            listOf(
                GenerationParamOptions.Tool(
                    name = "get_current_weather",
                    description = "获取指定地区的天气",
                    schema = AigcUtils.generateSchema(GetWeatherTool::class.java),
                ),
                GenerationParamOptions.Tool(
                    name = "get_current_time",
                    description = "获取当前时刻的时间",
                    schema = AigcUtils.generateSchema(GetTimeTool::class.java),
                ),
            )
        ).collectLatest { messages ->
            println(messages)
        }
    }

    @Test
    @Disabled
    fun testCreateStreamMultiModalMessage() = runBlocking {
        val messages =
            mutableListOf(
                Message(
                    role = MessageRole.SYSTEM,
                    contents = listOf(
                        MultiModalMessageContent.Text("你是灵祇，一个全能的AI助手。请简短的回答。")
                    )
                ),
                Message(
                    role = MessageRole.USER,
                    contents = listOf(
                        MultiModalMessageContent.Text("请用最少的话回答。你是谁?"),
                    )
                )
            )
        createStreamMultiModalMessage(messages).collectLatest {
            println(it)
        }
    }
}
