package com.zmkn.module.aliyunllm.audio

import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesizer
import org.apache.commons.pool2.impl.GenericObjectPool
import org.apache.commons.pool2.impl.GenericObjectPoolConfig

class SpeechSynthesizerObjectPool(
    private val size: Int,
) {
    val pool: GenericObjectPool<SpeechSynthesizer> by lazy {
        val speechSynthesizerObjectFactory = SpeechSynthesizerObjectFactory()
        val config = GenericObjectPoolConfig<SpeechSynthesizer>()
        config.maxTotal = size
        config.maxIdle = size
        config.minIdle = size
        GenericObjectPool(speechSynthesizerObjectFactory, config)
    }
}
