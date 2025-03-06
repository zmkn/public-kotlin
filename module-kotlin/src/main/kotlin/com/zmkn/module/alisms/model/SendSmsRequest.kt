package com.zmkn.module.alisms.model

data class SendSmsRequest(
    val phoneNumber: String,
    val signName: String,
    val templateCode: String,
    val templateParam: Map<String, String>,
)
