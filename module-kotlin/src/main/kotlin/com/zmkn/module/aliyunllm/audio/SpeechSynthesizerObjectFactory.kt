package com.zmkn.module.aliyunllm.audio

import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesizer
import org.apache.commons.pool2.BasePooledObjectFactory
import org.apache.commons.pool2.PooledObject
import org.apache.commons.pool2.impl.DefaultPooledObject

class SpeechSynthesizerObjectFactory : BasePooledObjectFactory<SpeechSynthesizer>() {
    override fun create(): SpeechSynthesizer = SpeechSynthesizer()

    override fun wrap(speechSynthesizer: SpeechSynthesizer): PooledObject<SpeechSynthesizer> = DefaultPooledObject(speechSynthesizer)
}
