package com.zmkn.module.alillm.model

import com.zmkn.module.alillm.enumeration.MessageRole

data class MultiModalConversationParamOptions(
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
    // 允许模型生成的最大Token数。
    val maxTokens: Int? = null,
    // 设置seed参数会使文本生成过程更具有确定性，通常用于使模型每次运行的结果一致。取值范围：无符号64位整数
    val seed: Int? = null,
    // 在流式输出模式下是否开启增量输出
    val incrementalOutput: Boolean? = null,
    // 用于控制模型在生成文本时是否使用互联网搜索结果进行参考。
    val enableSearch: Boolean? = null,
) {
    init {
        require(messages.isNotEmpty()) { "Property 'messages' must not be empty." }
        temperature?.let {
            require(it in 0.0..<2.0) { "Property 'temperature' must be greater than or equal to 0 and less than 2, but was $it." }
        }
        topP?.let {
            require(it > 0 && it <= 1) { "Property 'topP' must be greater than 0 and less than or equal to 1, but was $it." }
        }
        seed?.let {
            require(it in 0..Int.MAX_VALUE) { "Property 'seed' must be between 0 and ${Int.MAX_VALUE}, but was $it." }
        }
    }

    data class Message(
        val role: com.zmkn.module.alillm.enumeration.MessageRole,
        val contents: List<MultiModalMessageContent>,
    )
}
