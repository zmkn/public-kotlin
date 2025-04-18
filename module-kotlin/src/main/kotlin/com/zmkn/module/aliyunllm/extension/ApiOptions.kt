package com.zmkn.module.aliyunllm.extension

import com.alibaba.dashscope.protocol.ConnectionConfigurations
import com.zmkn.module.aliyunllm.model.ApiOptions
import java.time.Duration

fun ApiOptions.ConnectionOptions.toConnectionConfigurations(): ConnectionConfigurations = ConnectionConfigurations.builder()
    .also {
        if (connectTimeout != null) {
            it.connectTimeout(Duration.ofSeconds(connectTimeout))
        }
        if (readTimeout != null) {
            it.readTimeout(Duration.ofSeconds(readTimeout))
        }
        if (writeTimeout != null) {
            it.writeTimeout(Duration.ofSeconds(writeTimeout))
        }
        if (connectionIdleTimeout != null) {
            it.connectionIdleTimeout(Duration.ofSeconds(connectionIdleTimeout))
        }
        if (connectionPoolSize != null) {
            it.connectionPoolSize(connectionPoolSize)
        }
        if (maximumAsyncRequests != null) {
            it.maximumAsyncRequests(maximumAsyncRequests)
        }
        if (maximumAsyncRequestsPerHost != null) {
            it.maximumAsyncRequestsPerHost(maximumAsyncRequestsPerHost)
        }
    }
    .build()
