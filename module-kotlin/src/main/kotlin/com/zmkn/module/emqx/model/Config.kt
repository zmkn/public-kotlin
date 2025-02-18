package com.zmkn.module.emqx.model

data class Config(
    // Api Key
    val key: String,
    // Api Secret
    val secret: String,
    // 请求 Emqx Api 基础路径，例如：http://localhost:18084/api/v5
    val baseUrl: String,
)
