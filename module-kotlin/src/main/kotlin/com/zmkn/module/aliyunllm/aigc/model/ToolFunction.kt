package com.zmkn.module.aliyunllm.aigc.model

data class ToolFunction(
    val name: String,
    val description: String,
    val schema: String,
)
