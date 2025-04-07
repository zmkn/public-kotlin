package com.zmkn.module.aliyunllm.model

import com.zmkn.module.aliyunllm.enumeration.MessageRole

data class GenerationParamOptions(
    // 模型名称。
    val model: String,
    // 由历史对话组成的消息列表。
    val messages: List<Message>,
    // 采样温度，用于控制模型生成文本的多样性。取值范围： [0, 2)。 temperature越高，生成的文本更多样，反之，生成的文本更确定。
    val temperature: Float? = null,
    // 核采样的概率阈值，用于控制模型生成文本的多样性。取值范围：（0,1.0]。 top_p越高，生成的文本更多样。反之，生成的文本更确定。
    val topP: Double? = null,
    // 生成过程中采样候选集的大小。例如，取值为50时，仅将单次生成中得分最高的50个Token组成随机采样的候选集。取值越大，生成的随机性越高；取值越小，生成的确定性越高。取值为None或当top_k大于100时，表示不启用top_k策略，此时仅有top_p策略生效。
    val topK: Int? = null,
    // 用于控制模型生成时连续序列中的重复度。提高repetition_penalty时可以降低模型生成的重复度，1.0表示不做惩罚。没有严格的取值范围，只要大于0即可。
    val repetitionPenalty: Float? = null,
    // 允许模型生成的最大Token数。
    val maxTokens: Int? = null,
    // 设置seed参数会使文本生成过程更具有确定性，通常用于使模型每次运行的结果一致。取值范围：无符号64位整数
    val seed: Int? = null,
    // 在流式输出模式下是否开启增量输出
    val incrementalOutput: Boolean? = null,
    // 使用stop参数后，当模型生成的文本即将包含指定的字符串或token_id时，将自动停止生成。
    val stopStrings: List<String>? = null,
    // stopStrings 与 stopTokens 互斥，不可同时使用。
    val stopTokens: List<List<Int>>? = null,
    // 用于指定可供模型调用的工具数组，可以包含一个或多个工具对象。
    val tools: List<Tool>? = null,
    // 在使用tools参数时，用于控制模型调用指定工具。
    val toolChoice: ToolChoice? = null,
    // 用于控制模型在生成文本时是否使用互联网搜索结果进行参考。
    val enableSearch: Boolean? = null,
    // 联网搜索的策略。仅当enableSearch为true时生效。
    val searchOptions: SearchOptions? = null,
) {
    init {
        require(messages.isNotEmpty()) { "Property 'messages' must not be empty." }
        temperature?.let {
            require(it in 0.0..<2.0) { "Property 'temperature' must be greater than or equal to 0 and less than 2, but was $it." }
        }
        topP?.let {
            require(it > 0 && it <= 1) { "Property 'topP' must be greater than 0 and less than or equal to 1, but was $it." }
        }
        repetitionPenalty?.let {
            require(it > 0) { "Property 'repetitionPenalty' must be greater than 0, but was $it." }
        }
        seed?.let {
            require(it in 0..Int.MAX_VALUE) { "Property 'seed' must be between 0 and ${Int.MAX_VALUE}, but was $it." }
        }
        require(stopStrings == null || stopTokens == null) { "Property 'stopStrings' and 'stopTokens' are mutually exclusive." }
    }

    data class Message(
        val role: MessageRole,
        val content: String,
        val toolCallId: String? = null,
        val toolCalls: List<ToolCall>? = null,
    ) {
        data class ToolCall(
            val id: String,
            val type: String,
            val function: Function,
        ) {
            data class Function(
                val name: String,
                val arguments: String,
                val output: String,
            )
        }
    }

    data class Tool(
        val name: String,
        val description: String,
        val schema: String,
    )

    data class ToolChoice(
        val type: String = "function",
        val function: Function,
    ) {
        init {
            require(type == "function") { "Property 'type' must be 'function', but was '$type'" }
        }

        data class Function(
            val name: String,
        )
    }

    data class SearchOptions(
        // 在返回结果中是否展示搜索到的信息
        val enableSource: Boolean? = null,
        // 是否开启[1] 或[ref_1] 样式的角标标注功能。在enable_source为true时生效。
        val enableCitation: Boolean? = null,
        // 角标样式。在enable_citation为true时生效。[ ]：角标形式为[1], [ref_ ]：角标形式为[ref_1]。 默认为[ ]
        val citationFormat: String? = null,
        // 是否强制开启搜索
        val forcedSearch: Boolean? = null,
        // 搜索互联网信息的数量。standard：在请求时搜索5条互联网信息; pro：在请求时搜索10条互联网信息。 默认值为standard
        val searchStrategy: SearchStrategy? = null,
    ) {
        enum class SearchStrategy(val value: String) {
            STANDARD("standard"),
            PRO("pro");

            override fun toString(): String {
                return value
            }

            companion object {
                fun fromValue(value: String): SearchStrategy {
                    return when (value) {
                        STANDARD.value -> STANDARD
                        PRO.value -> PRO
                        else -> throw IllegalArgumentException("SearchStrategy value is not allowed.")
                    }
                }
            }
        }
    }
}
