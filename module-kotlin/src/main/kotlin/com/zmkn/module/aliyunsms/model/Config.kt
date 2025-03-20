package com.zmkn.module.aliyunsms.model

data class Config(
    val id: String,
    val secret: String,
    val protocol: String = "https",
    val endpoint: String = "dysmsapi.aliyuncs.com",
)
