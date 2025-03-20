package com.zmkn.module.aliyunllm

import com.alibaba.dashscope.aigc.generation.Generation
import com.alibaba.dashscope.aigc.generation.GenerationParam
import com.alibaba.dashscope.aigc.generation.GenerationResult
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversation
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationParam
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationResult
import com.alibaba.dashscope.common.Message
import com.alibaba.dashscope.common.MultiModalMessage
import com.alibaba.dashscope.common.Role
import com.alibaba.dashscope.tools.FunctionDefinition
import com.alibaba.dashscope.tools.ToolFunction
import com.alibaba.dashscope.utils.JsonUtils
import com.github.victools.jsonschema.generator.*
import com.zmkn.module.aliyunllm.enumeration.MessageRole
import com.zmkn.module.aliyunllm.enumeration.ResponseCode
import com.zmkn.module.aliyunllm.extension.*
import com.zmkn.module.aliyunllm.model.*
import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import java.lang.reflect.Type

class AliyunLlm(
    private val apiKeys: List<String>
) {
    init {
        require(apiKeys.isNotEmpty()) { "Parameter 'apiKeys' must not be empty." }
    }

    private fun getApiKey(index: Int): String = if (index in apiKeys.indices) {
        apiKeys[index]
    } else {
        throw IndexOutOfBoundsException("Index $index is out of bounds. Valid indices are 0 to ${apiKeys.size - 1}.")
    }

    private fun createGenerationParam(
        apiKeyIndex: Int,
        options: GenerationParamOptions
    ): GenerationParam {
        val messages: List<Message> = options.messages.map { message ->
            when (message.role) {
                MessageRole.SYSTEM -> createSystemMessage(message.content)
                MessageRole.USER -> createUserMessage(message.content)
                MessageRole.ASSISTANT -> createAssistantMessage(message.content, message.toolCalls)
                MessageRole.TOOL -> createToolMessage(message.toolCallId!!, message.content)
            }
        }
        return GenerationParam
            .builder()
            .apiKey(getApiKey(apiKeyIndex))
            .model(options.model)
            .messages(messages)
            .resultFormat(GenerationParam.ResultFormat.MESSAGE)
            .apply {
                options.temperature?.also {
                    temperature(it)
                }
                options.topP?.also {
                    topP(it)
                }
                options.topK?.also {
                    topK(it)
                }
                options.repetitionPenalty?.also {
                    repetitionPenalty(it)
                }
                options.maxTokens?.also {
                    maxTokens(it)
                }
                options.seed?.also {
                    seed(it)
                }
                options.incrementalOutput?.also {
                    incrementalOutput(it)
                }
                options.stopStrings?.also {
                    stopStrings(it)
                }
                options.stopTokens?.also {
                    stopTokens(it)
                }
                options.tools?.also {
                    val newTools = it.map { tool ->
                        createToolFunction(tool)
                    }
                    tools(newTools)
                }
                options.toolChoice?.also {
                    toolChoice(it)
                }
                options.enableSearch?.also {
                    enableSearch(it)
                }
                options.searchOptions?.also {
                    searchOptions(it.toSearchOptions())
                }
            }.build()
    }

    private fun createMultiModalConversationParam(
        apiKeyIndex: Int,
        options: MultiModalConversationParamOptions
    ): MultiModalConversationParam {
        val messages: List<MultiModalMessage> = options.messages.map { message ->
            when (message.role) {
                MessageRole.SYSTEM -> createMultiModalSystemMessage(message.contents)
                MessageRole.USER -> createMultiModalUserMessage(message.contents)
                MessageRole.ASSISTANT -> createMultiModalAssistantMessage(message.contents)
                MessageRole.TOOL -> throw IllegalArgumentException("MessageRole.TOOL is not allowed.")
            }
        }
        return MultiModalConversationParam
            .builder()
            .apiKey(getApiKey(apiKeyIndex))
            .model(options.model)
            .messages(messages)
            .apply {
                options.temperature?.also {
                    temperature(it)
                }
                options.topP?.also {
                    topP(it)
                }
                options.topK?.also {
                    topK(it)
                }
                options.repetitionPenalty?.also {
                    repetitionPenalty(it)
                }
                options.presencePenalty?.also {
                    presencePenalty(it)
                }
                options.maxTokens?.also {
                    maxTokens(it)
                }
                options.seed?.also {
                    seed(it)
                }
                options.incrementalOutput?.also {
                    incrementalOutput(it)
                }
                options.enableSearch?.also {
                    enableSearch(it)
                }
                options.modalities?.also {
                    modalities(it)
                }
                options.audio?.also {
                    audio(it.toAudioParameters())
                }
            }.build()
    }

    private fun createStreamMessage(
        apiKeyIndex: Int,
        options: GenerationParamOptions
    ): Flow<ResponseMessage> {
        val generation = Generation()
        val param = createGenerationParam(apiKeyIndex, options)
        val result: Flowable<GenerationResult> = generation.streamCall(param)
        return result
            .asFlow()
            .map {
                it.toResponseMessage()
            }.catch { e ->
                val requestException = RequestException(e)
                val responseCode = requestException.responseCode
                if (responseCode.statusCode == ResponseCode.INVALID_API_KEY.statusCode && responseCode.code == ResponseCode.INVALID_API_KEY.code && apiKeyIndex + 1 < apiKeys.size) {
                    emitAll(createStreamMessage(apiKeyIndex + 1, options))
                } else {
                    throw requestException
                }
            }
    }

    private fun createStreamMultiModalMessage(
        apiKeyIndex: Int,
        options: MultiModalConversationParamOptions
    ): Flow<ResponseMessage> {
        val multiModalConversation = MultiModalConversation()
        val param = createMultiModalConversationParam(apiKeyIndex, options)
        val result: Flowable<MultiModalConversationResult> = multiModalConversation.streamCall(param)
        return result
            .asFlow()
            .map {
                it.toResponseMessage()
            }.catch { e ->
                val requestException = RequestException(e)
                val responseCode = requestException.responseCode
                if (responseCode.statusCode == ResponseCode.INVALID_API_KEY.statusCode && responseCode.code == ResponseCode.INVALID_API_KEY.code && apiKeyIndex + 1 < apiKeys.size) {
                    emitAll(createStreamMultiModalMessage(apiKeyIndex + 1, options))
                } else {
                    throw requestException
                }
            }
    }

    fun createStreamMessage(options: GenerationParamOptions): Flow<ResponseMessage> = createStreamMessage(0, options)

    fun createStreamMultiModalMessage(options: MultiModalConversationParamOptions): Flow<ResponseMessage> = createStreamMultiModalMessage(0, options)

    companion object {
        private val schemaGeneratorConfigBuilder = SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON)
        private val schemaGeneratorConfig =
            schemaGeneratorConfigBuilder
                .with(Option.EXTRA_OPEN_API_FORMAT_VALUES)
                .without(Option.FLATTENED_ENUMS_FROM_TOSTRING)
                .build()
        private val schemaGenerator = SchemaGenerator(schemaGeneratorConfig)

        private fun createFunctionDefinition(options: GenerationParamOptions.Tool): FunctionDefinition = FunctionDefinition
            .builder()
            .name(options.name)
            .description(options.description)
            .parameters(JsonUtils.parseString(options.schema).asJsonObject)
            .build()

        fun generateSchema(
            mainTargetType: Type,
            vararg typeParameters: Type
        ): String = schemaGenerator.generateSchema(mainTargetType, *typeParameters).toString()

        fun createSystemMessage(content: String): Message = Message
            .builder()
            .role(Role.SYSTEM.value)
            .content(content)
            .build()

        fun createUserMessage(content: String): Message = Message
            .builder()
            .role(Role.USER.value)
            .content(content)
            .build()

        fun createAssistantMessage(content: String, toolCalls: List<GenerationParamOptions.Message.ToolCall>?): Message = Message
            .builder()
            .role(Role.ASSISTANT.value)
            .content(content)
            .also {
                toolCalls?.run {
                    it.toolCalls(map { it.toToolCallBase() })
                }
            }
            .build()

        fun createAssistantMessage(content: String): Message = createAssistantMessage(content, null)

        fun createToolMessage(toolCallId: String, content: String): Message = Message
            .builder()
            .role(Role.TOOL.value)
            .content(content)
            .toolCallId(toolCallId)
            .build()

        fun createMultiModalSystemMessage(contents: List<MultiModalMessageContent>): MultiModalMessage {
            val content = contents.map { it.toMap() }
            return MultiModalMessage
                .builder()
                .role(Role.SYSTEM.value)
                .content(content)
                .build()
        }

        fun createMultiModalSystemMessage(vararg contents: MultiModalMessageContent): MultiModalMessage = createMultiModalSystemMessage(contents.toList())

        fun createMultiModalUserMessage(contents: List<MultiModalMessageContent>): MultiModalMessage {
            val content = contents.map { it.toMap() }
            return MultiModalMessage
                .builder()
                .role(Role.USER.value)
                .content(content)
                .build()
        }

        fun createMultiModalUserMessage(vararg contents: MultiModalMessageContent): MultiModalMessage = createMultiModalUserMessage(contents.toList())

        fun createMultiModalAssistantMessage(contents: List<MultiModalMessageContent>): MultiModalMessage {
            val content = contents.map { it.toMap() }
            return MultiModalMessage
                .builder()
                .role(Role.ASSISTANT.value)
                .content(content)
                .build()
        }

        fun createMultiModalAssistantMessage(vararg contents: MultiModalMessageContent) = createMultiModalAssistantMessage(contents.toList())

        fun createToolFunction(options: GenerationParamOptions.Tool): ToolFunction {
            val functionDefinition = createFunctionDefinition(options)
            return ToolFunction
                .builder()
                .function(functionDefinition)
                .build()
        }
    }
}
