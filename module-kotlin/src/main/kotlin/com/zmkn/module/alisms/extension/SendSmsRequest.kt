package com.zmkn.module.alisms.extension

import com.zmkn.extension.toJson
import com.zmkn.module.alisms.model.SendSmsRequest
import com.aliyun.dysmsapi20170525.models.SendSmsRequest as AliSmsSendSmsRequest

fun SendSmsRequest.toAliSmsSendSmsRequest(): AliSmsSendSmsRequest {
    return AliSmsSendSmsRequest()
        .setPhoneNumbers(mobileNumber)
        .setSignName(signName)
        .setTemplateCode(templateCode)
        .setTemplateParam(templateParam.toJson())
}
