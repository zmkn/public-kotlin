package com.zmkn.module.okhttp.listener

import com.zmkn.module.okhttp.enumeration.NacosNameResolveAlgorithm
import com.zmkn.module.okhttp.interfaces.InterceptRequestListener
import com.zmkn.srcc.nacos.NacosNameResolver
import com.zmkn.srcc.nacos.util.NacosNameResolverUtils
import okhttp3.Interceptor
import okhttp3.Request

class NacosNameResolveInterceptRequestListener(
    private val scheme: String,
    private val algorithm: NacosNameResolveAlgorithm = NacosNameResolveAlgorithm.WEIGHTED,
    private val isEphemeral: Boolean = false,
    private val metadata: Map<String, String> = mapOf(),
) : InterceptRequestListener {
    override fun handler(chain: Interceptor.Chain, request: Request): Request {
        val originalUrl = request.url
        val originalScheme = originalUrl.scheme
        return if (originalScheme == NacosNameResolver.DEFAULT_SCHEME) {
            val serviceName = originalUrl.host
            when (algorithm) {
                NacosNameResolveAlgorithm.WEIGHTED -> NacosNameResolverUtils.getWeightedInstance(
                    serviceName = serviceName,
                    isEphemeral = isEphemeral,
                    metadata = metadata,
                )

                NacosNameResolveAlgorithm.ROUND_ROBIN -> NacosNameResolverUtils.getRoundRobinInstance(
                    serviceName = serviceName,
                    isEphemeral = isEphemeral,
                    metadata = metadata,
                )
            }?.let {
                val newUrl = originalUrl.newBuilder()
                    .scheme(scheme)
                    .host(it.ip)
                    .port(it.port)
                    .build()
                request.newBuilder().url(newUrl).build()
            } ?: request
        } else {
            request
        }
    }
}
