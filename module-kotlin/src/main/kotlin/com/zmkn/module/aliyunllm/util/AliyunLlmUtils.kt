package com.zmkn.module.aliyunllm.util

import com.alibaba.dashscope.utils.Constants
import com.zmkn.module.aliyunllm.extension.toConnectionConfigurations
import com.zmkn.module.aliyunllm.model.ApiOptions

object AliyunLlmUtils {
    fun setApiConfigurations(apiOptions: ApiOptions) {
        apiOptions.version?.also {
            Constants.apiVersion = it
        }
        apiOptions.region?.also {
            Constants.apiRegion = it
        }
        apiOptions.baseHttpApiUrl?.also {
            Constants.baseHttpApiUrl = it
        }
        apiOptions.baseWebsocketApiUrl?.also {
            Constants.baseWebsocketApiUrl = it
        }
        apiOptions.maxConnectionsHttp?.also {
            Constants.max_connections_http = it
        }
        apiOptions.maxConnectionsPerRouteHttp?.also {
            Constants.max_connections_per_route_http = it
        }
        apiOptions.connectionOptions?.also {
            setConnectionConfigurations(it)
        }
    }

    fun setConnectionConfigurations(connectionOptions: ApiOptions.ConnectionOptions) {
        Constants.connectionConfigurations = connectionOptions.toConnectionConfigurations()
    }
}
