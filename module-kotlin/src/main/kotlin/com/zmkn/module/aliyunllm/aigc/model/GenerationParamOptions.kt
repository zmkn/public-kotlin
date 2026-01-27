package com.zmkn.module.aliyunllm.aigc.model

import com.zmkn.module.aliyunllm.aigc.enumeration.MessageRole

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
    val tools: List<ToolFunction>? = null,
    // 在使用tools参数时，用于控制模型调用指定工具。
    val toolChoice: ToolChoice? = null,
    // 是否开启并行工具调用。默认值为 false。
    val parallelToolCalls: Boolean? = null,
    // 用于控制模型在生成文本时是否使用互联网搜索结果进行参考。
    val enableSearch: Boolean? = null,
    // 联网搜索的策略。仅当enableSearch为true时生效。
    val searchOptions: SearchOptions? = null,
    // 返回内容的格式。默认值为{"type": "text"}。
    val responseFormat: ResponseFormat? = null,
    // 使用混合思考模型时，是否开启思考模式，适用于 Qwen3 、Qwen3-VL模型。
    val enableThinking: Boolean? = null,
    // 思考过程的最大长度。适用于Qwen3-VL、Qwen3 的商业版与开源版模型。默认值为模型最大思维链长度。
    val thinkingBudget: Int? = null,
    // 是否返回输出 Token 的对数概率。
    val logprobs: Boolean? = null,
    // 指定在每一步生成时，返回模型最大概率的候选 Token 个数。取值范围：[0,5]。
    val topLogprobs: Int? = null,
    // 生成响应的个数，取值范围是1-4。对于需要生成多个响应的场景（如创意写作、广告文案等），可以设置较大的 n 值。
    val n: Int? = null,
    // 翻译参数
    val translationOptions: TranslationOptions? = null,
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
        topLogprobs?.let {
            require(it in 0..5) { "Property 'topLogprobs' must be between 0 and 5, but was $it." }
        }
        n?.let {
            require(it in 1..4) { "Property 'n' must be between 1 and 4, but was $it." }
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

            override fun toString(): String = value

            companion object {
                fun fromValue(value: String): SearchStrategy = when (value) {
                    STANDARD.value -> STANDARD
                    PRO.value -> PRO
                    else -> throw IllegalArgumentException("SearchStrategy value is not allowed.")
                }
            }
        }
    }

    data class TranslationOptions(
        // 源语言的英文全称
        val sourceLang: String,
        // 目标语言的英文全称
        val targetLang: String,
        // 在使用领域提示功能时需要设置的领域提示语句
        val domains: String? = null,
        // 在使用术语干预翻译功能时需要设置的术语数组
        val terms: List<Term>? = null,
        // 在使用翻译记忆功能时需要设置的翻译记忆数组
        val tmList: List<Tm>? = null,
    ) {
        data class Tm(
            // 源语句
            val source: String,
            // 已翻译的语句
            val target: String,
        )

        data class Term(
            // 术语
            val source: String,
            // 提前翻译好的术语
            val target: String,
        )
    }
}
