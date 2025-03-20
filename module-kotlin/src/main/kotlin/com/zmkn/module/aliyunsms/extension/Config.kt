package com.zmkn.module.aliyunsms.extension

import com.zmkn.module.aliyunsms.model.Config
import com.aliyun.teaopenapi.models.Config as AliyunSmsConfig

fun Config.toAliyunSmsConfig(): AliyunSmsConfig {
    return AliyunSmsConfig()
        .setAccessKeyId(id)
        .setAccessKeySecret(secret)
        .setProtocol(protocol)
        .setEndpoint(endpoint)
}
