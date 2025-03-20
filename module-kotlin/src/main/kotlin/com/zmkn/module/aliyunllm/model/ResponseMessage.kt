package com.zmkn.module.aliyunllm.model

import com.zmkn.module.aliyunllm.enumeration.MessageRole
import com.zmkn.module.aliyunllm.enumeration.ResponseMessageChoiceFinishReason

data class ResponseMessage(
    val requestId: String,
    val usage: Usage,
    val choices: List<Choice>,
) {
    data class Usage(
        val inputTokens: Int,
        val outputTokens: Int,
        val totalTokens: Int?,
    )

    data class Choice(
        val finishReason: ResponseMessageChoiceFinishReason?,
        val message: Message,
    ) {
        data class Message(
            val role: MessageRole,
            val content: String,
            val toolCalls: List<ToolCall>?,
        ) {
            data class ToolCall(
                val id: String,
                val type: String,
                val function: Function,
            ) {
                data class Function(
                    val name: String,
                    val arguments: String,
                )
            }
        }
    }
}
