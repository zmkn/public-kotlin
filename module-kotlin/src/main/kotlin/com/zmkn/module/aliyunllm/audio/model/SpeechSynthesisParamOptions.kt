package com.zmkn.module.aliyunllm.audio.model

import com.zmkn.module.aliyunllm.audio.enumeration.LanguageHint

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
    // 音频码率（单位kbps）。默认值：32。 取值范围：[6, 510]。
    val bitRate: Int? = null,
    // 是否开启字级别时间戳。默认值：false
    val enableWordTimestamp: Boolean? = null,
    // 启用音素时间戳。默认值：false
    val enablePhonemeTimestamp: Boolean? = null,
    // 生成时使用的随机数种子，使合成的效果产生变化。在模型版本、文本、音色及其他参数均相同的前提下，使用相同的seed可复现相同的合成结果。默认值0。取值范围：[0, 65535]。
    val seed: Int? = null,
    /*
     * 指定语音合成的目标语言，提升合成效果。cosyvoice-v1不支持该功能。
     * 当数字、缩写、符号等朗读方式或者小语种合成效果不符合预期时使用，例如：
     * 数字朗读方式不符合预期，“hello, this is 110”读成“hello, this is one one zero”而非“hello, this is 幺幺零”
     * 符号朗读不准确，“@”读成“艾特”而非“at”
     * 小语种合成效果差，合成不自然
     * 取值范围：
     * zh：中文
     * en：英文
     * fr：法语
     * de：德语
     * ja：日语
     * ko：韩语
     * ru：俄语
     * 注意：此参数为数组，但当前版本仅处理第一个元素，因此建议只传入一个值。
     */
    val languageHints: List<LanguageHint>? = null,
    // 设置指令，用于控制方言、情感或角色等合成效果。该功能仅适用于cosyvoice-v3-flash和cosyvoice-v3-plus模型的复刻音色，以及音色列表中标记为支持Instruct的系统音色。
    val instruction: String? = null,
    // 是否在生成的音频中添加AIGC隐性标识。默认值：false
    val enableAigcTag: Boolean? = null,
    // 设置AIGC隐性标识中的 ContentPropagator 字段，用于标识内容的传播者。仅在 enable_aigc_tag 为 true 时生效。默认值：阿里云UID。
    val aigcPropagator: String? = null,
    // 设置AIGC隐性标识中的 PropagateID 字段，用于唯一标识一次具体的传播行为。仅在 enable_aigc_tag 为 true 时生效。默认值：本次语音合成请求Request ID。
    val aigcPropagateId: String? = null,
) {
    init {
        volume?.let {
            require(it in 0..100) { "Property 'volume' must be greater than or equal to 0 and less than or equal to 100, but was $it." }
        }
        speechRate?.let {
            require(it in 0.5..2.0) { "Property 'speechRate' must be greater than or equal to 0.5 and less than or equal to 2, but was $it." }
        }
        pitchRate?.let {
            require(it in 0.5..2.0) { "Property 'pitchRate' must be greater than or equal to 0.5 and less than or equal to 2, but was $it." }
        }
        bitRate?.let {
            require(it in 6..510) { "Property 'bitRate' must be greater than or equal to 6 and less than or equal to 510, but was $it." }
        }
        seed?.let {
            require(it in 0..65535) { "Property 'seed' must be greater than or equal to 0 and less than or equal to 65535, but was $it." }
        }
    }

    enum class TextType(val value: String) {
        PLAIN_TEXT("PlainText"),
        SSML("SSML");

        override fun toString(): String = value

        companion object {
            fun fromValue(value: String): TextType = when (value) {
                PLAIN_TEXT.value -> PLAIN_TEXT
                SSML.value -> SSML
                else -> throw IllegalArgumentException("TextType value is not allowed.")
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

        override fun toString(): String = name
    }
}
