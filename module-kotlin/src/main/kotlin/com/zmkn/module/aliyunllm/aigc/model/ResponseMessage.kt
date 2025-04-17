package com.zmkn.module.aliyunllm.aigc.model

import com.zmkn.module.aliyunllm.aigc.enumeration.MessageRole
import com.zmkn.module.aliyunllm.aigc.enumeration.ResponseMessageChoiceFinishReason

data class ResponseMessage(
    val requestId: String,
    val usage: Usage,
    val output: Output,
) {
    data class Usage(
        val inputTokens: Int,
        val outputTokens: Int,
        val totalTokens: Int?,
    )

    data class Output(
        val choices: List<Choice>,
        val searchInfo: SearchInfo?,
    ) {
        data class SearchInfo(
            val searchResults: List<SearchResult>,
        ) {
            data class SearchResult(
                val index: Int,
                val siteName: String,
                val icon: String,
                val title: String,
                val url: String,
            )
        }

        data class Choice(
            val finishReason: ResponseMessageChoiceFinishReason?,
            val message: Message,
        ) {
            data class Message(
                val role: MessageRole,
                val content: String,
                val reasoningContent: String?,
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
                        val output: String,
                    )
                }
            }
        }
    }
}
