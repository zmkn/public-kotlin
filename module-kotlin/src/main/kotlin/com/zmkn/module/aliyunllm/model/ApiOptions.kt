package com.zmkn.module.aliyunllm.model

data class ApiOptions(
    val version: String? = null,
    val region: String? = null,
    val baseHttpApiUrl: String? = null,
    val baseWebsocketApiUrl: String? = null,
    val maxConnectionsHttp: Int? = null,
    val maxConnectionsPerRouteHttp: Int? = null,
    val connectionOptions: ConnectionOptions? = null,
) {
    data class ConnectionOptions(
        // 建立连接的超时时间，默认 120 秒
        val connectTimeout: Long? = null,
        // 读取数据的超时时间，默认 300 秒
        val readTimeout: Long? = null,
        // 写入数据的超时时间，默认 60 秒
        val writeTimeout: Long? = null,
        // 连接池中空闲连接的超时时间，默认 300 秒
        val connectionIdleTimeout: Long? = null,
        // 连接池中的最大连接数，默认 32
        val connectionPoolSize: Int? = null,
        // 最大并发请求数，默认 32
        val maximumAsyncRequests: Int? = null,
        // 单个主机的最大并发请求数，默认 32
        val maximumAsyncRequestsPerHost: Int? = null,
    )
}
