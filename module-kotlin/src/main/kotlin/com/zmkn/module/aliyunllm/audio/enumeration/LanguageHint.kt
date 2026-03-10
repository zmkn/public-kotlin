package com.zmkn.module.aliyunllm.audio.enumeration

import com.fasterxml.jackson.annotation.JsonValue
import kotlinx.serialization.SerialName

enum class LanguageHint(
    @JsonValue
    val value: String,
    val description: String,
) {
    @SerialName("zh")
    ZH("zh", "中文"),

    @SerialName("en")
    EN("en", "英文"),

    @SerialName("fr")
    FR("fr", "法语"),

    @SerialName("de")
    DE("de", "德语"),

    @SerialName("ja")
    JA("ja", "日语"),

    @SerialName("ko")
    KO("ko", "韩语"),

    @SerialName("ru")
    RU("ru", "俄语"),

    @SerialName("pt")
    PT("pt", "葡萄牙语"),

    @SerialName("th")
    TH("th", "泰语"),

    @SerialName("id")
    ID("id", "印尼语"),

    @SerialName("vi")
    VI("vi", "越南语");

    override fun toString(): String = value

    companion object {
        fun fromValue(
            value: String,
        ): LanguageHint = when (value) {
            ZH.value -> ZH
            EN.value -> EN
            FR.value -> FR
            DE.value -> DE
            JA.value -> JA
            KO.value -> KO
            RU.value -> RU
            PT.value -> PT
            TH.value -> TH
            ID.value -> ID
            VI.value -> VI
            else -> throw IllegalArgumentException("LanguageHint value is not allowed.")
        }
    }
}
