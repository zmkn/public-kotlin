package com.zmkn.module.alisms.model

data class SendSmsRequest(
    val mobileNumber: String,
    val signName: String,
    val templateCode: String,
    val templateParam: Map<String, String>,
)
