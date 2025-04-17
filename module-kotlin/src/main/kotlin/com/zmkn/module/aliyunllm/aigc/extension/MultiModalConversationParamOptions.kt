package com.zmkn.module.aliyunllm.aigc.extension

import com.alibaba.dashscope.aigc.multimodalconversation.AudioParameters
import com.alibaba.dashscope.aigc.multimodalconversation.OcrOptions
import com.google.gson.JsonObject
import com.zmkn.module.aliyunllm.aigc.model.MultiModalConversationParamOptions
import com.zmkn.module.aliyunllm.aigc.model.MultiModalConversationParamOptions.AudioParameters.Voice.*
import com.zmkn.module.aliyunllm.aigc.model.MultiModalConversationParamOptions.OcrOptions.Task.*

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

fun MultiModalConversationParamOptions.OcrOptions.toOcrOptions(): OcrOptions = OcrOptions.builder()
    .also {
        it.task(task.toOcrOptionsTask())
        if (taskConfig != null) {
            it.taskConfig(taskConfig.toOcrOptionsTaskConfig())
        }
    }
    .build()

fun MultiModalConversationParamOptions.OcrOptions.Task.toOcrOptionsTask(): OcrOptions.Task = when (this) {
    KEY_INFORMATION_EXTRACTION -> OcrOptions.Task.KEY_INFORMATION_EXTRACTION
    TEXT_RECOGNITION -> OcrOptions.Task.TEXT_RECOGNITION
    TABLE_PARSING -> OcrOptions.Task.TABLE_PARSING
    DOCUMENT_PARSING -> OcrOptions.Task.DOCUMENT_PARSING
    FORMULA_RECOGNITION -> OcrOptions.Task.FORMULA_RECOGNITION
    MULTI_LAN -> OcrOptions.Task.MULTI_LAN
}

fun MultiModalConversationParamOptions.OcrOptions.TaskConfig.toOcrOptionsTaskConfig(): OcrOptions.TaskConfig = OcrOptions.TaskConfig.builder()
    .also {
        if (resultSchema.isNotEmpty()) {
            val resultSchemaJsonObject = JsonObject()
            resultSchema.forEach {
                resultSchemaJsonObject.addProperty(it.first, it.second)
            }
            it.resultSchema(resultSchemaJsonObject)
        }
    }
    .build()
