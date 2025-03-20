package com.zmkn.module.aliyunllm.model

sealed class MultiModalMessageContent {
    data class Text(val text: String) : MultiModalMessageContent()

    data class Image(val image: String) : MultiModalMessageContent()

    data class Audio(val audio: String) : MultiModalMessageContent()

    data class Video(val videos: List<String>) : MultiModalMessageContent() {
        init {
            require(videos.size in 4..768) { "Video list must contain between 4 and 768 elements, but has ${videos.size}." }
        }
    }

    data class ImageOcr(
        val image: String,
        val minPixels: Int = 3136,
        val maxPixels: Int = 1003520,
    ) : MultiModalMessageContent() {
        init {
            require(maxPixels in (minPixels + 1)..23520000) { "The input image's maximum pixel size must be greater than the minimum pixel size and ≤ 23,520,000, but it is $maxPixels." }
            require(minPixels in 1..<maxPixels) { "The input image's minimum pixel size must be ≥ 1 and less than maximum pixel size, but it is $minPixels." }
        }
    }
}
