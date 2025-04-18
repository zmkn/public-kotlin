package com.zmkn.module.aliyunllm.audio.model

import java.net.URI

private fun isUrl(str: String): Boolean {
    return try {
        val uri = URI(str)
        uri.scheme != null && uri.host != null
    } catch (_: Exception) {
        false
    }
}

data class VoiceEnrollmentCreateOptions(
    // 声音复刻所使用的模型，固定为cosyvoice-v1。
    val model: String,
    // 音色自定义前缀，仅允许数字和小写字母，小于十个字符。
    val prefix: String,
    // 用于复刻音色的音频文件URL。该URL要求公网可访问。
    val url: String,
) {
    init {
        require(prefix.matches(Regex("^[a-z0-9]{1,9}$"))) { "prefix must be 1-9 lowercase letters and/or numbers." }
        require(isUrl(url)) { "url must be a url address." }
    }
}
