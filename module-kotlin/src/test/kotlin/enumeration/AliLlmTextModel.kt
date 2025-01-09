package enumeration

enum class AliLlmTextModel(
    val code: String,
    val types: List<AliLlmModelType>,
    val outputType: AliLlmModelOutputType,
    val title: String,
    val free: Boolean,
    val tokenTotal: Int,
    val inputToken: Int,
    val outputToken: Int,
) {
    QwenMax(
        "qwen-max",
        listOf(AliLlmModelType.Text),
        AliLlmModelOutputType.Text,
        "通义千问-Max",
        false,
        32768,
        30720,
        8192,
    ),
    QwenMaxLatest(
        "qwen-max-latest",
        listOf(AliLlmModelType.Text),
        AliLlmModelOutputType.Text,
        "通义千问-Max-Latest",
        false,
        32768,
        30720,
        8192,
    ),
    QwenPlus(
        "qwen-plus",
        listOf(AliLlmModelType.Text),
        AliLlmModelOutputType.Text,
        "通义千问-Plus",
        false,
        131072,
        129024,
        8192,
    ),
    QwenPlusLatest(
        "qwen-plus-latest",
        listOf(AliLlmModelType.Text),
        AliLlmModelOutputType.Text,
        "通义千问-Plus-Latest",
        false,
        131072,
        129024,
        8192,
    ),
    QwenTurbo(
        "qwen-turbo",
        listOf(AliLlmModelType.Text),
        AliLlmModelOutputType.Text,
        "通义千问-Turbo",
        false,
        131072,
        129024,
        8192,
    ),
    QwenTurboLatest(
        "qwen-turbo-latest",
        listOf(AliLlmModelType.Text),
        AliLlmModelOutputType.Text,
        "通义千问-Turbo-Latest",
        false,
        1000000,
        1000000,
        8192,
    ),
    QwenLong(
        "qwen-long",
        listOf(AliLlmModelType.Text),
        AliLlmModelOutputType.Text,
        "Qwen-Long",
        false,
        10000000,
        10000000,
        6000,
    ),
    QwenVlMax(
        "qwen-vl-max",
        listOf(AliLlmModelType.InputImage, AliLlmModelType.InputVideo),
        AliLlmModelOutputType.Text,
        "通义千问VL-Max",
        false,
        32000,
        30000,
        2000,
    ),
    QwenVlMaxLatest(
        "qwen-vl-max-latest",
        listOf(AliLlmModelType.InputImage, AliLlmModelType.InputVideo),
        AliLlmModelOutputType.Text,
        "通义千问VL-Max-Latest",
        false,
        32000,
        30000,
        2000,
    ),
    QwenVlPlus(
        "qwen-vl-plus",
        listOf(AliLlmModelType.InputImage),
        AliLlmModelOutputType.Text,
        "通义千问VL-Plus",
        false,
        32000,
        30000,
        2000,
    ),
    QwenVlPlusLatest(
        "qwen-vl-plus-latest",
        listOf(AliLlmModelType.InputImage),
        AliLlmModelOutputType.Text,
        "通义千问VL-Plus-Latest",
        false,
        32000,
        30000,
        2000,
    ),
    QwenVlOcr(
        "qwen-vl-ocr",
        listOf(AliLlmModelType.ImageOcr),
        AliLlmModelOutputType.Text,
        "通义千问VL-OCR",
        false,
        34096,
        30000,
        4096,
    ),
    QwenVlOcrLatest(
        "qwen-vl-ocr-latest",
        listOf(AliLlmModelType.ImageOcr),
        AliLlmModelOutputType.Text,
        "通义千问VL-OCR-Latest",
        false,
        34096,
        30000,
        4096,
    ),
    QwenMathPlus(
        "qwen-math-plus",
        listOf(AliLlmModelType.Text),
        AliLlmModelOutputType.Text,
        "通义千问-Math-Plus",
        false,
        4096,
        3072,
        3072,
    ),
    QwenMathPlusLatest(
        "qwen-math-plus-latest",
        listOf(AliLlmModelType.Text),
        AliLlmModelOutputType.Text,
        "通义千问-Math-Plus-Latest",
        false,
        4096,
        3072,
        3072,
    ),
    QwenMathTurbo(
        "qwen-math-turbo",
        listOf(AliLlmModelType.Text),
        AliLlmModelOutputType.Text,
        "通义千问-Math-Turbo",
        false,
        4096,
        3072,
        3072,
    ),
    QwenMathTurboLatest(
        "qwen-math-turbo-latest",
        listOf(AliLlmModelType.Text),
        AliLlmModelOutputType.Text,
        "通义千问-Math-Turbo-Latest",
        false,
        4096,
        3072,
        3072,
    ),
    QwenCoderPlus(
        "qwen-coder-plus",
        listOf(AliLlmModelType.Text),
        AliLlmModelOutputType.Text,
        "通义千问-Coder-Plus",
        false,
        131072,
        129024,
        8192,
    ),
    QwenCoderPlusLatest(
        "qwen-coder-plus-latest",
        listOf(AliLlmModelType.Text),
        AliLlmModelOutputType.Text,
        "通义千问-Coder-Plus-Latest",
        false,
        131072,
        129024,
        8192,
    ),
    QwenCoderTurbo(
        "qwen-coder-turbo",
        listOf(AliLlmModelType.Text),
        AliLlmModelOutputType.Text,
        "通义千问-Coder-Turbo",
        false,
        131072,
        129024,
        8192,
    ),
    QwenCoderTurboLatest(
        "qwen-coder-turbo-latest",
        listOf(AliLlmModelType.Text),
        AliLlmModelOutputType.Text,
        "通义千问-Coder-Turbo-Latest",
        false,
        131072,
        129024,
        8192,
    ),
    ;

    companion object {
        fun byCode(code: String): AliLlmTextModel {
            return when (code) {
                QwenMax.code -> QwenMax
                QwenMaxLatest.code -> QwenMaxLatest
                QwenPlus.code -> QwenPlus
                QwenPlusLatest.code -> QwenPlusLatest
                QwenTurbo.code -> QwenTurbo
                QwenTurboLatest.code -> QwenTurboLatest
                QwenLong.code -> QwenLong
                QwenVlMax.code -> QwenVlMax
                QwenVlMaxLatest.code -> QwenVlMaxLatest
                QwenVlPlus.code -> QwenVlPlus
                QwenVlPlusLatest.code -> QwenVlPlusLatest
                QwenVlOcr.code -> QwenVlOcr
                QwenVlOcrLatest.code -> QwenVlOcrLatest
                QwenMathPlus.code -> QwenMathPlus
                QwenMathPlusLatest.code -> QwenMathPlusLatest
                QwenMathTurbo.code -> QwenMathTurbo
                QwenMathTurboLatest.code -> QwenMathTurboLatest
                QwenCoderPlus.code -> QwenCoderPlus
                QwenCoderPlusLatest.code -> QwenCoderPlusLatest
                QwenCoderTurbo.code -> QwenCoderTurbo
                QwenCoderTurboLatest.code -> QwenCoderTurboLatest
                else -> throw IllegalArgumentException("AliLlmModel code [$code] not recognized.")
            }
        }
    }
}
