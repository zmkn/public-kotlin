package com.zmkn.module.aliyunsms

import com.aliyun.dysmsapi20170525.Client
import com.zmkn.module.aliyunsms.extension.toAliyunSmsConfig
import com.zmkn.module.aliyunsms.extension.toAliyunSmsSendSmsRequest
import com.zmkn.module.aliyunsms.extension.toSendSmsResponse
import com.zmkn.module.aliyunsms.model.Config
import com.zmkn.module.aliyunsms.model.SendSmsRequest
import com.zmkn.module.aliyunsms.model.SendSmsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AliyunSms(config: Config) {
    val client = Client(config.toAliyunSmsConfig())

    fun sendSync(sendSmsRequest: SendSmsRequest): SendSmsResponse {
        return client.sendSms(sendSmsRequest.toAliyunSmsSendSmsRequest()).toSendSmsResponse()
    }

    suspend fun send(sendSmsRequest: SendSmsRequest): SendSmsResponse = withContext(Dispatchers.IO) {
        sendSync(sendSmsRequest)
    }
}
