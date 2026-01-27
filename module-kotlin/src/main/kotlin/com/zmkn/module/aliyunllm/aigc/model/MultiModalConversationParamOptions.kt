package com.zmkn.module.aliyunllm.aigc.model

import com.zmkn.module.aliyunllm.aigc.enumeration.MessageRole

data class MultiModalConversationParamOptions(
    // 模型名称。
    val model: String,
    // 由历史对话组成的消息列表。
    val messages: List<Message>,
    // 采样温度，用于控制模型生成文本的多样性。取值范围： [0, 2)。 temperature越高，生成的文本更多样，反之，生成的文本更确定。
    val temperature: Float? = null,
    // 核采样的概率阈值，用于控制模型生成文本的多样性。取值范围：（0,1.0]。 top_p越高，生成的文本更多样。反之，生成的文本更确定。
    val topP: Double? = null,
    // 生成过程中采样候选集的大小。例如，取值为50时，仅将单次生成中得分最高的50个Token组成随机采样的候选集。取值越大，生成的随机性越高；取值越小，生成的确定性越高。取值为None或当top_k大于100时，表示不启用top_k策略，此时仅有top_p策略生效。
    val topK: Int? = null,
    // 用于控制模型生成时连续序列中的重复度。提高repetition_penalty时可以降低模型生成的重复度，1.0表示不做惩罚。没有严格的取值范围，只要大于0即可。
    val repetitionPenalty: Float? = null,
    /**
     * 控制模型生成文本时的内容重复度。
     * 取值范围：[-2.0, 2.0]。正数会减少重复度，负数会增加重复度。
     * 适用场景：
     * 较高的presence_penalty适用于要求多样性、趣味性或创造性的场景，如创意写作或头脑风暴。
     * 较低的presence_penalty适用于要求一致性或专业术语的场景，如技术文档或其他正式文档。
     */
    val presencePenalty: Float? = null,
    // 允许模型生成的最大Token数。
    val maxTokens: Int? = null,
    // 设置seed参数会使文本生成过程更具有确定性，通常用于使模型每次运行的结果一致。取值范围：无符号64位整数
    val seed: Int? = null,
    // 在流式输出模式下是否开启增量输出
    val incrementalOutput: Boolean? = null,
    // 用于控制模型在生成文本时是否使用互联网搜索结果进行参考。
    val enableSearch: Boolean? = null,
    // 模型输出格式包括“text”和“audio”，默认值：["text"]
    val modalities: List<Modality>? = null,
    // 音频输出参数
    val audio: AudioParameters? = null,
    // OCR选项
    val ocrOptions: OcrOptions? = null,
    // 输入的文本
    val text: String? = null,
    // voice of tts
    val voice: AudioParameters.Voice? = null,
    // 用于指定可供模型调用的工具数组，可以包含一个或多个工具对象。
    val tools: List<ToolFunction>? = null,
    // 在使用tools参数时，用于控制模型调用指定工具。
    val toolChoice: ToolChoice? = null,
    // 是否开启并行工具调用。默认值为 false。
    val parallelToolCalls: Boolean? = null,
    // 是否将输入图像的像素上限提升至 16384 Token 对应的像素值。默认值为 false。
    // vl_high_resolution_images：true，使用固定分辨率策略，忽略 max_pixels 设置，超过此分辨率时会将图像总像素缩小至此上限内。
    // vl_high_resolution_images为false，像素上限由 max_pixels 决定，输入图像的像素超过max_pixels会将图像缩小至max_pixels内。各模型的默认像素上限即max_pixels的默认值。
    val vlHighResolutionImages: Boolean? = null,
    // 是否返回图像缩放后的尺寸。模型会对输入的图像进行缩放处理，配置为 True 时会返回图像缩放后的高度和宽度，开启流式输出时，该信息在最后一个数据块（chunk）中返回。默认值为 false。
    val vlEnableImageHwOutput: Boolean? = null,
    // 返回内容的格式。默认值为{"type": "text"}。
    val responseFormat: ResponseFormat? = null,
    // 反向提示词。描述不希望在画面中出现的内容，如“模糊”、“多余的手指”等。
    val negativePrompt: String? = null,
    // 是否开启提示词智能改写。开启后，将使用大模型优化正向提示词，对较短的提示词有明显提升效果，但增加3-4秒耗时。默认值为 true。
    val promptExtend: Boolean? = null,
    // 是否添加水印标识，水印位于图片右下角，文案固定为“AI生成”。默认值为 false。
    val watermark: Boolean? = null,
    /*
     * 输出图像的分辨率，格式为宽*高。例如：
     * 1:1：1280*1280
     * 3:4：1104*1472
     * 4:3：1472*1104
     * 9:16：960*1696
     * 16:9：1696*960
     */
    val size: String? = null,
    // 生成图片的数量。取值范围为1~4张，默认为4。测试阶段建议设置为1，便于低成本验证。
    val n: Int? = null,
    // 指定合成音频的语种，默认为 Auto。
    val languageType: LanguageType? = null,
    // 使用混合思考模型时，是否开启思考模式，适用于 Qwen3 、Qwen3-VL模型。
    val enableThinking: Boolean? = null,
    // 思考过程的最大长度。适用于Qwen3-VL、Qwen3 的商业版与开源版模型。默认值为模型最大思维链长度。
    val thinkingBudget: Int? = null,
) {
    init {
        require(messages.isNotEmpty()) { "Property 'messages' must not be empty." }
        temperature?.let {
            require(it in 0.0..<2.0) { "Property 'temperature' must be greater than or equal to 0 and less than 2, but was $it." }
        }
        topP?.let {
            require(it > 0 && it <= 1) { "Property 'topP' must be greater than 0 and less than or equal to 1, but was $it." }
        }
        repetitionPenalty?.let {
            require(it > 0) { "Property 'repetitionPenalty' must be greater than 0, but was $it." }
        }
        presencePenalty?.let {
            require(it >= -2.0 && it <= 2.0) { "Property 'presencePenalty' must be greater than or equal to -2.0 and less than or equal to 2.0, but was $it." }
        }
        seed?.let {
            require(it in 0..Int.MAX_VALUE) { "Property 'seed' must be between 0 and ${Int.MAX_VALUE}, but was $it." }
        }
        n?.let {
            require(it in 1..4) { "Property 'n' must be between 1 and 4, but was $it." }
        }
    }

    enum class Modality(val value: String) {
        TEXT("text"),
        AUDIO("audio");

        override fun toString(): String = value
    }

    enum class LanguageType(val value: String, val description: String) {
        AUTO("Auto", "自动"),
        CHINESE("Chinese", "中文"),
        ENGLISH("English", "英文"),
        GERMAN("German", "德语"),
        ITALIAN("Italian", "意大利语"),
        PORTUGUESE("Portuguese", "葡萄牙语"),
        SPANISH("Spanish", "西班牙语"),
        JAPANESE("Japanese", "日语"),
        KOREAN("Korean", "韩语"),
        FRENCH("French", "法语"),
        RUSSIAN("Russian", "俄语");

        override fun toString(): String = value
    }

    data class Message(
        val role: MessageRole,
        val contents: List<MultiModalMessageContent>,
    )

    data class AudioParameters(
        val voice: Voice? = null,
    ) {
        enum class Voice(val value: String) {
            CHERRY("Cherry"),
            SERENA("Serena"),
            ETHAN("Ethan"),
            CHELSIE("Chelsie")
        }
    }

    data class OcrOptions(
        val task: Task,
        val taskConfig: TaskConfig? = null,
    ) {
        enum class Task(val value: String) {
            KEY_INFORMATION_EXTRACTION("KEY_INFORMATION_EXTRACTION"),
            TEXT_RECOGNITION("TEXT_RECOGNITION"),
            TABLE_PARSING("TABLE_PARSING"),
            DOCUMENT_PARSING("DOCUMENT_PARSING"),
            FORMULA_RECOGNITION("FORMULA_RECOGNITION"),
            MULTI_LAN("MULTI_LAN")
        }

        data class TaskConfig(
            val resultSchema: List<Pair<String, String>>,
        )
    }
}
