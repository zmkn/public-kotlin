package com.zmkn.module.aliyunllm

open class Base(
    private val apiKeys: List<String>
) {
    init {
        require(apiKeys.isNotEmpty()) { "Parameter 'apiKeys' must not be empty." }
    }

    internal fun getApiKey(index: Int): String = if (index in apiKeys.indices) {
        apiKeys[index]
    } else {
        throw IndexOutOfBoundsException("Index $index is out of bounds. Valid indices are 0 to ${apiKeys.size - 1}.")
    }
}
