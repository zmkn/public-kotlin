import com.alibaba.dashscope.utils.JsonUtils
import com.zmkn.module.aliyunllm.AliyunLlm
import com.zmkn.module.aliyunllm.enumeration.ResponseMessageChoiceFinishReason
import com.zmkn.module.aliyunllm.extension.toGenerationParamOptionsMessage
import com.zmkn.module.aliyunllm.extension.toMultiModalConversationParamOptionsMessage
import com.zmkn.module.aliyunllm.model.GenerationParamOptions
import com.zmkn.module.aliyunllm.model.MultiModalConversationParamOptions
import com.zmkn.module.aliyunllm.model.MultiModalConversationParamOptions.Message
import com.zmkn.module.aliyunllm.model.MultiModalMessageContent
import com.zmkn.module.aliyunllm.model.ResponseMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.runBlocking
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

class AliyunLlmTest {
    private val _aliyunLlm = AliyunLlm(listOf("11111111", ""))

    private suspend fun createStreamMessageAndTools(messages: List<GenerationParamOptions.Message>, tools: List<GenerationParamOptions.Tool>? = null): Flow<ResponseMessage> {
        println("开始-createStreamMessageAndTools")
        println(messages)
        val options =
            GenerationParamOptions(
                model = "qwen-plus",
                messages = messages,
                tools = tools,
            )
        return _aliyunLlm.createStreamMessage(options).transform { result ->
            val choice = result.choices[0]
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
                                role = com.zmkn.module.aliyunllm.enumeration.MessageRole.TOOL,
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
                                role = com.zmkn.module.aliyunllm.enumeration.MessageRole.TOOL,
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

    private fun createStreamMultiModalMessage(messages: List<Message>): Flow<ResponseMessage> {
        println("开始-createStreamMultiModalMessage")
        println(messages)
        val options =
            MultiModalConversationParamOptions(
                model = "qwen-vl-max-latest",
                messages = messages
            )
        return _aliyunLlm.createStreamMultiModalMessage(options)
    }

    //    @Test
    fun testCreateStreamMessage() = runBlocking {
        createStreamMessageAndTools(
            listOf(
                GenerationParamOptions.Message(
                    role = com.zmkn.module.aliyunllm.enumeration.MessageRole.SYSTEM,
                    content = "你是灵祇，一个全能的AI助手。"
                ),
                GenerationParamOptions.Message(
                    role = com.zmkn.module.aliyunllm.enumeration.MessageRole.USER,
                    content = "你是谁"
                )
            )
        ).collect {
            println(it.requestId)
            println(it.usage)
            println(it.choices)
            println(JsonUtils.toJson(it))
        }
    }

    //    @Test
    fun testCreateStreamMessageAndTools() = runBlocking {
        createStreamMessageAndTools(
            listOf(
                GenerationParamOptions.Message(
                    role = com.zmkn.module.aliyunllm.enumeration.MessageRole.SYSTEM,
                    content = "你是灵祇，一个全能的AI助手。"
                ),
                GenerationParamOptions.Message(
                    role = com.zmkn.module.aliyunllm.enumeration.MessageRole.USER,
                    content = "北京天气怎么样"
                )
            ),
            listOf(
                GenerationParamOptions.Tool(
                    name = "get_current_weather",
                    description = "获取指定地区的天气",
                    schema = AliyunLlm.generateSchema(GetWeatherTool::class.java),
                ),
                GenerationParamOptions.Tool(
                    name = "get_current_time",
                    description = "获取当前时刻的时间",
                    schema = AliyunLlm.generateSchema(GetTimeTool::class.java),
                ),
            )
        ).collectLatest { messages ->
            println(messages)
        }
    }

    //    @Test
    fun testCreateStreamMultiModalMessage() = runBlocking {
        val messages =
            mutableListOf(
                Message(
                    role = com.zmkn.module.aliyunllm.enumeration.MessageRole.SYSTEM,
                    contents = listOf(
                        MultiModalMessageContent.Text("你是灵祇，一个全能的AI助手。")
                    )
                ),
                Message(
                    role = com.zmkn.module.aliyunllm.enumeration.MessageRole.USER,
                    contents = listOf(
                        MultiModalMessageContent.Image("https://dashscope.oss-cn-beijing.aliyuncs.com/images/dog_and_girl.jpeg"),
                        MultiModalMessageContent.Image("https://dashscope.oss-cn-beijing.aliyuncs.com/images/tiger.png"),
                        MultiModalMessageContent.Text("这些是什么?"),
                    )
                )
            )
        createStreamMultiModalMessage(messages).transform {
            emit(it)
            val choice = it.choices[0]
            if (choice.finishReason == ResponseMessageChoiceFinishReason.STOP) {
                messages.add(choice.message.toMultiModalConversationParamOptionsMessage())
                messages.add(
                    Message(
                        role = com.zmkn.module.aliyunllm.enumeration.MessageRole.USER,
                        contents = listOf(
                            MultiModalMessageContent.Text("为第一张图写一段小故事"),
                        )
                    )
                )
                emitAll(createStreamMultiModalMessage(messages))
            }
        }.collectLatest {
            println(it)
        }
    }
}
