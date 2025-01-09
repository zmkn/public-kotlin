package enumeration

enum class AliLlmModelOutputType(val type: String, val title: String) {
    Text("Text", "文本生成"),
    Image("Image", "图片生成"),
    Audio("Audio", "音频生成"),
    Video("Video", "视频生成"),
}
