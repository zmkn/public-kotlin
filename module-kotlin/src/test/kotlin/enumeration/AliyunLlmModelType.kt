package enumeration

enum class AliyunLlmModelType(val type: String, val title: String) {
    Text("Text", "文本输入"),
    InputImage("InputImage", "图片输入"),
    InputAudio("InputAudio", "音频输入"),
    InputVideo("InputVideo", "视频输入"),
    ImageOcr("ImageOcr", "图片文字提取"),
}
