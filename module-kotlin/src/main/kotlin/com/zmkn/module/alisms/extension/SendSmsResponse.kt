package com.zmkn.module.alisms.extension

import com.zmkn.module.alisms.model.SendSmsResponse
import com.aliyun.dysmsapi20170525.models.SendSmsResponse as AliSmsSendSmsResponse

fun AliSmsSendSmsResponse.toSendSmsResponse(): SendSmsResponse {
    val body = body!!
    val message = body.message
    val status = if (statusCode == 200) {
        val code = body.code.lowercase()
        when (code) {
            "ok" -> {
                1
            }

            "Amount.NotEnough" -> {
                2
            }

            "DayLimitControl" -> {
                3
            }

            "InvalidParam.PhoneNumber", "InvalidParameter.phoneNumber" -> {
                4
            }

            "isv.BUSINESS_LIMIT_CONTROL" -> {
                5
            }

            else -> {
                0
            }
        }
    } else {
        0
    }
    return SendSmsResponse(status, message)
}
