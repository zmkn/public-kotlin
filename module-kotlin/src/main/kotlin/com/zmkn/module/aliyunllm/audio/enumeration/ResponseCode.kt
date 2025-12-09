package com.zmkn.module.aliyunllm.audio.enumeration

enum class ResponseCode(
    val statusCode: Int,
    val code: String,
    val message: String,
    val messageEn: String,
) {
    UNKNOWN_ERROR(0, "unknown_error", "未知异常", "Unknown Error."),
    NETWORK_ERROR(-1, "network error", "网络异常", "Network error."),
    INVALID_PARAMETER(44, "InvalidParameter", "请求参数错误", "Invalid request parameter."),
    INVALID_API_KEY(44, "ConnectionError", "请求中的 ApiKey 错误", "Invalid API-key provided."),
    MODEL_ACCESS_DENIED(44, "Model.AccessDenied", "模型拒绝访问", "Model access denied.");

    override fun toString(): String = "{\"statusCode\":$statusCode,\"code\":\"$code\",\"message\":\"$message\",\"messageEn\":\"$messageEn\"}"

    companion object {
        fun fromCodeAndStatusCode(
            code: String,
            statusCode: Int
        ): ResponseCode = when (code) {
            NETWORK_ERROR.code if statusCode == NETWORK_ERROR.statusCode -> NETWORK_ERROR
            INVALID_PARAMETER.code if statusCode == INVALID_PARAMETER.statusCode -> INVALID_PARAMETER
            INVALID_API_KEY.code if statusCode == INVALID_API_KEY.statusCode -> INVALID_API_KEY
            MODEL_ACCESS_DENIED.code if statusCode == MODEL_ACCESS_DENIED.statusCode -> MODEL_ACCESS_DENIED
            else -> UNKNOWN_ERROR
        }
    }
}
