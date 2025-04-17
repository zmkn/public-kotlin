package com.zmkn.module.aliyunllm.aigc

import com.alibaba.dashscope.aigc.generation.Generation
import com.alibaba.dashscope.aigc.generation.GenerationParam
import com.alibaba.dashscope.aigc.generation.GenerationResult
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversation
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationParam
import com.alibaba.dashscope.aigc.multimodalconversation.MultiModalConversationResult
import com.alibaba.dashscope.common.Message
import com.alibaba.dashscope.common.MultiModalMessage
import com.zmkn.module.aliyunllm.Base
import com.zmkn.module.aliyunllm.aigc.enumeration.MessageRole
import com.zmkn.module.aliyunllm.aigc.extension.*
import com.zmkn.module.aliyunllm.aigc.model.*
import com.zmkn.module.aliyunllm.aigc.util.AigcUtils
import com.zmkn.module.aliyunllm.enumeration.ResponseCode
import com.zmkn.module.aliyunllm.model.RequestException
import io.reactivex.Flowable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow

class Aigc(
    private val apiKeys: List<String>
) : Base(apiKeys) {
    private fun createGenerationParam(
        apiKeyIndex: Int,
        options: GenerationParamOptions
    ): GenerationParam {
        val messages: List<Message> = options.messages.map { message ->
            when (message.role) {
                MessageRole.SYSTEM -> AigcUtils.createSystemMessage(message.content)
                MessageRole.USER -> AigcUtils.createUserMessage(message.content)
                MessageRole.ASSISTANT -> AigcUtils.createAssistantMessage(message.content, message.toolCalls)
                MessageRole.TOOL -> AigcUtils.createToolMessage(message.toolCallId!!, message.content)
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
                        AigcUtils.createToolFunction(tool)
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
                MessageRole.SYSTEM -> AigcUtils.createMultiModalSystemMessage(message.contents)
                MessageRole.USER -> AigcUtils.createMultiModalUserMessage(message.contents)
                MessageRole.ASSISTANT -> AigcUtils.createMultiModalAssistantMessage(message.contents)
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
                    modalities(
                        it.map { modality ->
                            modality.value
                        }
                    )
                }
                options.audio?.also {
                    audio(it.toAudioParameters())
                }
                options.ocrOptions?.also {
                    ocrOptions(it.toOcrOptions())
                }
                options.voice?.also {
                    voice(it.toAudioParametersVoice())
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
    ): Flow<MultiModalResponseMessage> {
        val multiModalConversation = MultiModalConversation()
        val param = createMultiModalConversationParam(apiKeyIndex, options)
        val result: Flowable<MultiModalConversationResult> = multiModalConversation.streamCall(param)
        return result
            .asFlow()
            .map {
                it.toMultiModalResponseMessage()
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

    fun createStreamMultiModalMessage(options: MultiModalConversationParamOptions): Flow<MultiModalResponseMessage> = createStreamMultiModalMessage(0, options)
}
