package com.zmkn.module.alisms.extension

import com.zmkn.module.alisms.model.Config
import com.aliyun.teaopenapi.models.Config as AliSmsConfig

fun Config.toAliSmsConfig(): AliSmsConfig {
    return AliSmsConfig()
        .setAccessKeyId(id)
        .setAccessKeySecret(secret)
        .setProtocol(protocol)
        .setEndpoint(endpoint)
}
