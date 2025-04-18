package com.zmkn.module.aliyunllm

import com.zmkn.module.aliyunllm.model.ApiOptions
import com.zmkn.module.aliyunllm.util.AliyunLlmUtils

open class Base(
    private val apiKeys: List<String>,
    private val apiOptions: ApiOptions?,
) {
    init {
        require(apiKeys.isNotEmpty()) { "Parameter 'apiKeys' must not be empty." }
        apiOptions?.also {
            AliyunLlmUtils.setApiConfigurations(apiOptions)
        }
    }

    internal fun getApiKey(index: Int): String = if (index in apiKeys.indices) {
        apiKeys[index]
    } else {
        throw IndexOutOfBoundsException("Index $index is out of bounds. Valid indices are 0 to ${apiKeys.size - 1}.")
    }
}
