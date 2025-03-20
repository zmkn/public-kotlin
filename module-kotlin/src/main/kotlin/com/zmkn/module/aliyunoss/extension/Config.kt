package com.zmkn.module.aliyunoss.extension

import com.aliyun.oss.ClientBuilderConfiguration
import com.aliyun.oss.ClientConfiguration
import com.aliyun.oss.common.auth.CredentialsProvider
import com.aliyun.oss.common.auth.CredentialsProviderFactory
import com.aliyun.oss.common.comm.SignVersion
import com.zmkn.module.aliyunoss.model.Config

fun Config.toCredentialsProvider(): CredentialsProvider {
    return CredentialsProviderFactory.newDefaultCredentialProvider(id, secret)
}

fun Config.toClientConfiguration(): ClientConfiguration {
    return ClientBuilderConfiguration().also {
        it.signatureVersion = SignVersion.V4
        it.isSupportCname = cname
        it.protocol = protocol
        it.socketTimeout = socketTimeout
        it.maxConnections = maxConnections
    }
}
