package com.zmkn.module.aliyunllm.audio.model

data class SpeechSynthesisParamOptions(
    // 模型名称。
    val model: String,
    // 文本。
    val texts: List<String>,
    // 指定语音合成所使用的音色。
    val voice: String,
    // 输入的文本类型。
    val textType: TextType? = null,
    // 指定音频编码格式及采样率。
    val format: Format? = null,
    // 指定音量，取值范围：0~100。
    val volume: Int? = null,
    // 指定语速，取值范围：0.5~2。0.5：表示默认语速的0.5倍速。1：表示默认语速。默认语速是指模型默认输出的合成语速，语速会因音色不同而略有不同。约每秒钟4个字。2：表示默认语速的2倍速。
    val speechRate: Float? = null,
    // 指定语调，取值范围：0.5~2。
    val pitchRate: Float? = null,
    // 启用单词时间戳。默认值：false
    val enableWordTimestamp: Boolean? = null,
    // 启用音素时间戳。默认值：false
    val enablePhonemeTimestamp: Boolean? = null,
) {
    init {
        volume?.let {
            require(it >= 0 && it <= 100) { "Property 'volume' must be greater than or equal to 0 and less than or equal to 100, but was $it." }
        }
        speechRate?.let {
            require(it >= 0.5 && it <= 2) { "Property 'volume' must be greater than or equal to 0.5 and less than or equal to 2, but was $it." }
        }
        pitchRate?.let {
            require(it >= 0.5 && it <= 2) { "Property 'volume' must be greater than or equal to 0.5 and less than or equal to 2, but was $it." }
        }
    }

    enum class TextType(val value: String) {
        PLAIN_TEXT("PlainText"),
        SSML("SSML");

        override fun toString(): String {
            return value
        }

        companion object {
            fun fromValue(value: String): TextType {
                return when (value) {
                    PLAIN_TEXT.value -> PLAIN_TEXT
                    SSML.value -> SSML
                    else -> throw IllegalArgumentException("TextType value is not allowed.")
                }
            }
        }
    }

    enum class Format(
        val format: String,
        val sampleRate: Int,
        val channels: String,
        val bitRate: String,
    ) {
        DEFAULT("Default", 0, "0", "0"),
        WAV_8000HZ_MONO_16BIT("wav", 8000, "mono", "16bit"),
        WAV_16000HZ_MONO_16BIT("wav", 16000, "mono", "16bit"),
        WAV_22050HZ_MONO_16BIT("wav", 22050, "mono", "16bit"),
        WAV_24000HZ_MONO_16BIT("wav", 24000, "mono", "16bit"),
        WAV_44100HZ_MONO_16BIT("wav", 44100, "mono", "16bit"),
        WAV_48000HZ_MONO_16BIT("wav", 48000, "mono", "16bit"),
        MP3_8000HZ_MONO_128KBPS("mp3", 8000, "mono", "128kbps"),
        MP3_16000HZ_MONO_128KBPS("mp3", 16000, "mono", "128kbps"),
        MP3_22050HZ_MONO_256KBPS("mp3", 22050, "mono", "256kbps"),
        MP3_24000HZ_MONO_256KBPS("mp3", 24000, "mono", "256kbps"),
        MP3_44100HZ_MONO_256KBPS("mp3", 44100, "mono", "256kbps"),
        MP3_48000HZ_MONO_256KBPS("mp3", 48000, "mono", "256kbps"),
        PCM_8000HZ_MONO_16BIT("pcm", 8000, "mono", "16bit"),
        PCM_16000HZ_MONO_16BIT("pcm", 16000, "mono", "16bit"),
        PCM_22050HZ_MONO_16BIT("pcm", 22050, "mono", "16bit"),
        PCM_24000HZ_MONO_16BIT("pcm", 24000, "mono", "16bit"),
        PCM_44100HZ_MONO_16BIT("pcm", 44100, "mono", "16bit"),
        PCM_48000HZ_MONO_16BIT("pcm", 48000, "mono", "16bit");

        override fun toString(): String {
            return "{\"format\": \"$format\", \"sampleRate\": $sampleRate, \"channels\": \"$channels\", \"bitRate\": \"$bitRate\"}"
        }
    }
}
