package com.zmkn.module.aliyunllm.audio.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.zmkn.module.aliyunllm.audio.enumeration.LanguageHint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateVoiceOptions(
    // 声音设计所使用的模型，默认值：voice-enrollment。
    val model: String = "voice-enrollment",
    // 输入参数。
    val input: Input,
    // 额外参数。
    val parameters: Parameters = Parameters(),
) {
    @Serializable
    data class Input(
        // 动作，默认值：create_voice。例如：create_voice。
        val action: String = "create_voice",

        // 目标模型，例如：cosyvoice-v3.5-plus。
        @SerialName("target_model")
        @param:JsonProperty("target_model")
        val targetModel: String,

        // 用于设计音色时使用的提示词，长度不得超过 500 个字符。例如：“沉稳的中年男性播音员，音色低沉浑厚，富有磁性，语速平稳，吐字清晰，适合用于新闻播报或纪录片解说。”。
        @SerialName("voice_prompt")
        @param:JsonProperty("voice_prompt")
        val voicePrompt: String,

        // 目标音色生成的预览音频朗读的内容，长度不得超过 500 个字符。例如：“各位听众朋友，大家好，欢迎收听晚间新闻。”。
        @SerialName("preview_text")
        @param:JsonProperty("preview_text")
        val previewText: String,

        // 音色自定义前缀，仅允许数字和小写字母，小于十个字符，例如：announcer。
        val prefix: String,

        /*
         * 指定声音设计生成音色的语言倾向。该参数影响生成音色的语言特征和发音倾向，建议根据实际使用场景选择对应语言代码。
         * 若使用该参数，设置的语种要和preview_text的语种一致。
         * 取值范围：
         * zh：中文（默认值）
         * en：英文
         */
        @SerialName("language_hints")
        @param:JsonProperty("language_hints")
        val languageHints: List<LanguageHint>? = null,
    )

    @Serializable
    data class Parameters(
        // 音频码率。默认值：32000。 取值范围：[6000, 510000]，例如：24000。
        @SerialName("sample_rate")
        @param:JsonProperty("sample_rate")
        val sampleRate: Int = 32000,
        // 生成的音频格式，默认值：wav。例如：wav。
        @SerialName("response_format")
        @param:JsonProperty("response_format")
        val responseFormat: String = "wav",
    ) {
        init {
            require(sampleRate in 6000..510000) { "Property 'sampleRate' must be greater than or equal to 6000 and less than or equal to 510000, but was $sampleRate." }
        }
    }
}
