package com.zmkn.module.alillm.extension

import com.zmkn.module.alillm.model.MultiModalMessageContent

fun MultiModalMessageContent.toMap(): Map<String, Any> = when (this) {
    is MultiModalMessageContent.Text -> mapOf("text" to text)
    is MultiModalMessageContent.Image -> mapOf("image" to image)
    is MultiModalMessageContent.Audio -> mapOf("audio" to audio)
    is MultiModalMessageContent.Video -> mapOf("video" to videos)
    is MultiModalMessageContent.ImageOcr -> mapOf("image" to image, "min_pixels" to minPixels, "max_pixels" to maxPixels)
}
