package com.zmkn.module.alillm.extension

import com.alibaba.dashscope.aigc.multimodalconversation.AudioParameters
import com.zmkn.module.alillm.model.MultiModalConversationParamOptions
import com.zmkn.module.alillm.model.MultiModalConversationParamOptions.AudioParameters.Voice.*

fun MultiModalConversationParamOptions.AudioParameters.toAudioParameters(): AudioParameters = AudioParameters.builder().also { audioParameters ->
    voice?.let {
        audioParameters.voice(it.toAudioParametersVoice())
    }
}.build()

fun MultiModalConversationParamOptions.AudioParameters.Voice.toAudioParametersVoice(): AudioParameters.Voice {
    return when (this) {
        CHERRY -> AudioParameters.Voice.CHERRY
        SERENA -> AudioParameters.Voice.SERENA
        ETHAN -> AudioParameters.Voice.ETHAN
        CHELSIE -> AudioParameters.Voice.CHELSIE
    }
}
