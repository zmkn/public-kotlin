package com.zmkn.module.aliyunllm.model

import com.zmkn.module.aliyunllm.enumeration.MessageRole
import com.zmkn.module.aliyunllm.enumeration.ResponseMessageChoiceFinishReason

data class MultiModalResponseMessage(
    val requestId: String,
    val usage: Usage,
    val output: Output,
) {
    data class Usage(
        val inputTokens: Int,
        val outputTokens: Int,
        val totalTokens: Int?,
        val imageTokens: Int?,
        val videoTokens: Int?,
        val audioTokens: Int?,
        val inputTokensDetails: TokensDetails?,
        val outputTokensDetails: TokensDetails?,
    ) {
        data class TokensDetails(
            val textTokens: Int?,
            val imageTokens: Int?,
            val audioTokens: Int?,
            val videoTokens: Int?,
        )
    }

    data class Output(
        val choices: List<Choice>,
        val audio: Audio?,
    ) {
        data class Audio(
            val id: String,
            val url: String,
            val data: String,
            val expiresAt: Long,
        )

        data class Choice(
            val finishReason: ResponseMessageChoiceFinishReason?,
            val message: Message,
        ) {
            data class Message(
                val role: MessageRole,
                val content: List<Map<String, String>>,
                val reasoningContent: String?,
            )
        }
    }
}
