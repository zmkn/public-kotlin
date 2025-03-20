package com.zmkn.module.aliyunoss.model

import com.aliyun.oss.common.comm.Protocol

data class Config(
    val id: String,
    val secret: String,
    val bucket: String,
    val region: String,
    val endpoint: String,
    val cname: Boolean = true,
    val protocol: Protocol = Protocol.HTTPS,
    val socketTimeout: Int = 60000,
    val maxConnections: Int = 1024,
)
