package com.zmkn.module.aliyunsms.model

data class SendSmsRequest(
    val phoneNumber: String,
    val signName: String,
    val templateCode: String,
    val templateParam: Map<String, String>,
)
