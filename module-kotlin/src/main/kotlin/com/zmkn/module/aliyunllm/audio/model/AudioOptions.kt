package com.zmkn.module.aliyunllm.audio.model

data class AudioOptions(
    // 对象池大小
    val objectPoolSize: Int = 500,
    // 连接池大小
    val connectionPoolSize: Int = 1000,
    // 最大异步请求数
    val maximumAsyncRequests: Int = 1000,
    // 单host最大异步请求数
    val maximumAsyncRequestsPerHost: Int = 1000,
)
