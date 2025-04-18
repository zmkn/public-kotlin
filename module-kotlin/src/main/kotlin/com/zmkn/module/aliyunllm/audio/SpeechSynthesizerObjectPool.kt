package com.zmkn.module.aliyunllm.audio

import com.alibaba.dashscope.audio.ttsv2.SpeechSynthesizer
import org.apache.commons.pool2.impl.GenericObjectPool
import org.apache.commons.pool2.impl.GenericObjectPoolConfig

class SpeechSynthesizerObjectPool(
    private val objectPoolSize: Int = 500,
) {
    val synthesizerPool: GenericObjectPool<SpeechSynthesizer> by lazy {
        val speechSynthesizerObjectFactory = SpeechSynthesizerObjectFactory()
        val config = GenericObjectPoolConfig<SpeechSynthesizer>()
        config.maxTotal = objectPoolSize
        config.maxIdle = objectPoolSize
        config.minIdle = objectPoolSize
        GenericObjectPool(speechSynthesizerObjectFactory, config)
    }
}
