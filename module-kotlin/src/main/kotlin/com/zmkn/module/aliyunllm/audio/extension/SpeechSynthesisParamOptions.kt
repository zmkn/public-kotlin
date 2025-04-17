package com.zmkn.module.aliyunllm.audio.extension

import com.alibaba.dashscope.audio.tts.SpeechSynthesisTextType
import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesisAudioFormat
import com.zmkn.module.aliyunllm.audio.model.SpeechSynthesisParamOptions
import com.zmkn.module.aliyunllm.audio.model.SpeechSynthesisParamOptions.Format.*
import com.zmkn.module.aliyunllm.audio.model.SpeechSynthesisParamOptions.TextType.PLAIN_TEXT
import com.zmkn.module.aliyunllm.audio.model.SpeechSynthesisParamOptions.TextType.SSML

fun SpeechSynthesisParamOptions.TextType.toSpeechSynthesisTextType(): SpeechSynthesisTextType = when (this) {
    PLAIN_TEXT -> SpeechSynthesisTextType.PLAIN_TEXT
    SSML -> SpeechSynthesisTextType.SSML
}

fun SpeechSynthesisParamOptions.Format.toSpeechSynthesisAudioFormat(): SpeechSynthesisAudioFormat = when (this) {
    DEFAULT -> SpeechSynthesisAudioFormat.DEFAULT
    WAV_8000HZ_MONO_16BIT -> SpeechSynthesisAudioFormat.WAV_8000HZ_MONO_16BIT
    WAV_16000HZ_MONO_16BIT -> SpeechSynthesisAudioFormat.WAV_16000HZ_MONO_16BIT
    WAV_22050HZ_MONO_16BIT -> SpeechSynthesisAudioFormat.WAV_22050HZ_MONO_16BIT
    WAV_24000HZ_MONO_16BIT -> SpeechSynthesisAudioFormat.WAV_24000HZ_MONO_16BIT
    WAV_44100HZ_MONO_16BIT -> SpeechSynthesisAudioFormat.WAV_44100HZ_MONO_16BIT
    WAV_48000HZ_MONO_16BIT -> SpeechSynthesisAudioFormat.WAV_48000HZ_MONO_16BIT
    MP3_8000HZ_MONO_128KBPS -> SpeechSynthesisAudioFormat.MP3_8000HZ_MONO_128KBPS
    MP3_16000HZ_MONO_128KBPS -> SpeechSynthesisAudioFormat.MP3_16000HZ_MONO_128KBPS
    MP3_22050HZ_MONO_256KBPS -> SpeechSynthesisAudioFormat.MP3_22050HZ_MONO_256KBPS
    MP3_24000HZ_MONO_256KBPS -> SpeechSynthesisAudioFormat.MP3_24000HZ_MONO_256KBPS
    MP3_44100HZ_MONO_256KBPS -> SpeechSynthesisAudioFormat.MP3_44100HZ_MONO_256KBPS
    MP3_48000HZ_MONO_256KBPS -> SpeechSynthesisAudioFormat.MP3_48000HZ_MONO_256KBPS
    PCM_8000HZ_MONO_16BIT -> SpeechSynthesisAudioFormat.PCM_8000HZ_MONO_16BIT
    PCM_16000HZ_MONO_16BIT -> SpeechSynthesisAudioFormat.PCM_16000HZ_MONO_16BIT
    PCM_22050HZ_MONO_16BIT -> SpeechSynthesisAudioFormat.PCM_22050HZ_MONO_16BIT
    PCM_24000HZ_MONO_16BIT -> SpeechSynthesisAudioFormat.PCM_24000HZ_MONO_16BIT
    PCM_44100HZ_MONO_16BIT -> SpeechSynthesisAudioFormat.PCM_44100HZ_MONO_16BIT
    PCM_48000HZ_MONO_16BIT -> SpeechSynthesisAudioFormat.PCM_48000HZ_MONO_16BIT
}
