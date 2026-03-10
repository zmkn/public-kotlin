package com.zmkn.module.aliyunllm.audio.model

import com.zmkn.module.aliyunllm.audio.enumeration.LanguageHint
import com.zmkn.module.aliyunllm.util.AliyunLlmUtils

data class EnrollVoiceOptions(
    // 声音复刻所使用的模型，例如：cosyvoice-v3-plus。
    val model: String,
    // 音色自定义前缀，仅允许数字和小写字母，小于十个字符。
    val prefix: String,
    // 用于复刻音色的音频文件URL。该URL要求公网可访问。
    val url: String,
    /*
     * 指定用于提取目标音色特征的样本音频语种，仅适用于 cosyvoice-v3.5-plus、cosyvoice-v3.5-flash、cosyvoice-v3-flash 和 cosyvoice-v3-plus 模型。
     * 该参数用于辅助模型识别样本音频（原始参考音频）的语种，从而更准确地提取音色特征，提升复刻效果。
     * 若设置的语言提示与实际音频语言不符（例如为中文音频设置 en），系统将忽略此提示，并依据音频内容自动检测语言。
     * 取值范围（因模型而异）：
     * cosyvoice-v3-plus：zh（默认值）、en、fr、de、ja、ko、ru。
     * cosyvoice-v3.5-plus、cosyvoice-v3.5-flash、cosyvoice-v3-flash：zh（默认值）、en、fr、de、ja、ko、ru、pt、th、id、vi。
     * 当前版本仅处理第一个元素，建议只传入一个值。
     */
    val languageHints: List<LanguageHint>? = null,
) {
    init {
        require(prefix.matches(Regex("^[a-z0-9]{1,9}$"))) { "prefix must be 1-9 lowercase letters and/or numbers." }
        require(AliyunLlmUtils.isUrl(url)) { "url must be a valid url address." }
    }
}
