package com.zmkn.module.aliyunsms.extension

import com.zmkn.extension.toJson
import com.zmkn.module.aliyunsms.model.SendSmsRequest
import com.aliyun.dysmsapi20170525.models.SendSmsRequest as AliyunSmsSendSmsRequest

fun SendSmsRequest.toAliyunSmsSendSmsRequest(): AliyunSmsSendSmsRequest {
    return AliyunSmsSendSmsRequest()
        .setPhoneNumbers(phoneNumber)
        .setSignName(signName)
        .setTemplateCode(templateCode)
        .setTemplateParam(templateParam.toJson())
}
