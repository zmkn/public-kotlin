package com.zmkn.module.aliyunllm.audio.enumeration

enum class LanguageHint(
    val value: String,
    val description: String,
) {
    ZH("zh", "中文"),
    EN("en", "英文"),
    FR("fr", "法语"),
    DE("de", "德语"),
    JA("ja", "日语"),
    KO("ko", "韩语"),
    RU("ru", "俄语"),
    PT("pt", "葡萄牙语"),
    TH("th", "泰语"),
    ID("id", "印尼语"),
    VI("vi", "越南语");

    override fun toString(): String = name

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
