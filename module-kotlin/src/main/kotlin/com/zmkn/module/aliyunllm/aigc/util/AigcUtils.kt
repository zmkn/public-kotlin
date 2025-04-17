package com.zmkn.module.aliyunllm.aigc.util

import com.alibaba.dashscope.common.Message
import com.alibaba.dashscope.common.MultiModalMessage
import com.alibaba.dashscope.common.Role
import com.alibaba.dashscope.tools.FunctionDefinition
import com.alibaba.dashscope.tools.ToolFunction
import com.alibaba.dashscope.utils.JsonUtils
import com.github.victools.jsonschema.generator.*
import com.zmkn.module.aliyunllm.aigc.extension.toMap
import com.zmkn.module.aliyunllm.aigc.extension.toToolCallBase
import com.zmkn.module.aliyunllm.aigc.model.GenerationParamOptions
import com.zmkn.module.aliyunllm.aigc.model.MultiModalMessageContent
import java.lang.reflect.Type

object AigcUtils {
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
