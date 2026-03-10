package com.zmkn.module.aliyunllm.audio.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.zmkn.module.aliyunllm.audio.enumeration.CreateVoiceResponseFormat
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseCreatedVoice(
    @SerialName("request_id")
    @param:JsonProperty("request_id")
    val requestId: String,
    val usage: Usage,
    val output: Output,
) {
    @Serializable
    data class Usage(
        val count: Int,
    )

    @Serializable
    data class Output(
        // 音色名称
        @SerialName("voice_id")
        @param:JsonProperty("voice_id")
        val voiceId: String,

        // 生成的音频内容
        @SerialName("preview_audio")
        @param:JsonProperty("preview_audio")
        val previewAudio: PreviewAudio,
    ) {
        @Serializable
        data class PreviewAudio(
            // Base64编码的音频数据
            val data: String,

            // 生成的音频格式，例如：wav
            @SerialName("response_format")
            @param:JsonProperty("response_format")
            val responseFormat: CreateVoiceResponseFormat,

            // 音频码率。例如：32000
            @SerialName("sample_rate")
            @param:JsonProperty("sample_rate")
            val sampleRate: Int,
        )
    }
}
