package com.zmkn.module.aliyunllm.audio.enumeration

import com.fasterxml.jackson.annotation.JsonValue
import kotlinx.serialization.SerialName

enum class CreateVoiceResponseFormat(
    @JsonValue
    val value: String,
) {
    @SerialName("mp3")
    MP3("mp3"),

    @SerialName("wav")
    WAV("wav");

    override fun toString(): String = value

    companion object {
        fun fromValue(
            value: String,
        ): CreateVoiceResponseFormat = when (value) {
            MP3.value -> MP3
            WAV.value -> WAV
            else -> throw IllegalArgumentException("CreateVoiceResponseFormat value is not allowed.")
        }
    }
}
