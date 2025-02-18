package com.zmkn.module.alisms

import com.aliyun.dysmsapi20170525.Client
import com.zmkn.module.alisms.extension.toAliSmsConfig
import com.zmkn.module.alisms.extension.toAliSmsSendSmsRequest
import com.zmkn.module.alisms.extension.toSendSmsResponse
import com.zmkn.module.alisms.model.Config
import com.zmkn.module.alisms.model.SendSmsRequest
import com.zmkn.module.alisms.model.SendSmsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AliSms(config: Config) {
    val client = Client(config.toAliSmsConfig())

    fun sendSync(sendSmsRequest: SendSmsRequest): SendSmsResponse {
        return client.sendSms(sendSmsRequest.toAliSmsSendSmsRequest()).toSendSmsResponse()
    }

    suspend fun send(sendSmsRequest: SendSmsRequest): SendSmsResponse = withContext(Dispatchers.IO) {
        sendSync(sendSmsRequest)
    }
}
