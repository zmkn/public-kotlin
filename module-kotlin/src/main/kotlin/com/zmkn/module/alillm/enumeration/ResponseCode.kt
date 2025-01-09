package com.zmkn.module.alillm.enumeration

enum class ResponseCode(
    val statusCode: Int,
    val code: String,
    val message: String,
    val messageEn: String,
) {
    UNKNOWN_ERROR(0, "unknown_error", "未知异常", "Unknown Error."),
    NETWORK_ERROR(-1, "network error", "网络异常", "Network error."),
    RESPONSE_ERROR(400, "response_error", "响应异常", "Response error."),
    INVALID_PARAMETER(400, "InvalidParameter", "接口调用参数不合法", "Required parameter(s) missing or invalid, please check the request parameters."),
    DATA_INSPECTION_FAILED(400, "DataInspectionFailed", "数据检查错误，输入或者输出包含疑似敏感内容被绿网拦截。", "Input or output data may contain inappropriate content. Input data may contain inappropriate content. Output data may contain inappropriate content."),
    BAD_REQUEST_EMPTY_INPUT(400, "BadRequest.EmptyInput", "请求的输入不能为空", "Required input parameter missing from request."),
    BAD_REQUEST_EMPTY_PARAMETERS(400, "BadRequest.EmptyParameters", "请求的参数不能为空", "Required parameter 'parameters' missing from request."),
    BAD_REQUEST_EMPTY_MODEL(400, "BadRequest.EmptyModel", "请求输入的模型不能为空", "Required parameter 'model' missing from request."),
    INVALID_URL(400, "InvalidURL", "请求的URL错误", "Invalid URL provided in your request."),
    ARREARAGE(400, "Arrearage", "账户异常，访问被拒绝。", "Access denied, please make sure your account is in good standing."),
    UNSUPPORTED_OPERATION(400, "UnsupportedOperation", "关联的对象不支持该操作", "The operation is unsupported on the referee object."),
    FLOW_NOT_PUBLISHED(400, "FlowNotPublished", "流程未发布，请发布流程后再重试。", "Flow has not published yet, please publish flow and try again."),
    INVALID_SCHEMA(400, "InvalidSchema", "请输入数据库Schema信息", "Database schema is invalid for text2sql."),
    INVALID_SCHEMA_FORMAT(400, "InvalidSchemaFormat", "输入数据表信息格式异常", "Database schema format is invalid for text2sql."),
    FAQ_RULE_BLOCKED(400, "FaqRuleBlocked", "命中FAQ规则干预模块", "Input or output data is blocked by faq rule."),
    CUSTOM_ROLE_BLOCKED(400, "CustomRoleBlocked", "请求或响应内容没有通过自定义策略", "Input or output data may contain inappropriate content with custom rule."),
    INTERNAL_ERROR_ALGO_400(400, "InternalError.Algo", "URL请求的响应头信息缺失Content-Length字段", "Missing Content-Length of multimodal url."),
    INVALID_API_KEY(401, "InvalidApiKey", "请求中的 ApiKey 错误", "Invalid API-key provided."),
    ACCESS_DENIED(403, "AccessDenied", "无权访问此 API", "Access denied."),
    WORK_SPACE_ACCESS_DENIED(403, "Workspace.AccessDenied", "无权限访问业务空间的应用或者模型", "Workspace access denied."),
    MODEL_ACCESS_DENIED(403, "Model.AccessDenied", "子账号无权限访问业务空间的模型", "Model access denied."),
    ACCESS_DENIED_UNPURCHASED(403, "AccessDenied.Unpurchased", "账户异常，访问模型被拒绝。", "Access to model denied. Please make sure you are eligible for using the model."),
    WORK_SPACE_NOT_FOUND(404, "WorkSpaceNotFound", "用户空间信息不存在", "WorkSpace can not be found."),
    MODEL_NOT_FOUND(404, "ModelNotFound", "当前访问的模型不存在", "Model can not be found."),
    REQUEST_TIMEOUT(408, "RequestTimeOut", "请求超时，您可尝试再次发起请求。", "Request timed out, please try again later."),
    BAD_REQUEST_TOO_LARGE(413, "BadRequest.TooLarge", "接入层网关返回请求体过大", "Payload Too Large."),
    BAD_REQUEST_INPUT_DOWNLOAD_FAILED(415, "BadRequest.InputDownloadFailed", "下载输入文件失败", "Failed to download the input file: xxx."),
    BAD_REQUEST_UNSUPPORTED_FILE_FORMAT(415, "BadRequest.UnsupportedFileFormat", "输入文件的格式不受支持", "Input file format is not supported."),
    THROTTLING(429, "Throttling", "接口调用触发限流", "Requests throttling triggered."),
    THROTTLING_RATE_QUOTA(429, "Throttling.RateQuota", "调用频次触发限流，比如每秒钟请求次数。", "Requests rate limit exceeded, please try again later."),
    THROTTLING_ALLOCATION_QUOTA(429, "Throttling.AllocationQuota", "一段时间调用量触发限流，比如每分钟生成Token数。", "Allocated quota exceeded, please increase your quota limit."),
    LIMIT_REQUESTS(429, "LimitRequests", "超出调用限制，您需等到不满足限流条件时才能再次调用。", "You exceeded your current requests list."),
    PREPAID_BILL_OVERDUE(429, "PrepaidBillOverdue", "业务空间预付费账单到期", "The prepaid bill is overdue."),
    POSTPAID_BILL_OVERDUE(429, "PostpaidBillOverdue", "模型推理商品已失效", "The postpaid bill is overdue."),
    COMMODITY_NOT_PURCHASED(429, "CommodityNotPurchased", "业务空间未订购", "Commodity has not purchased yet."),
    INTERNAL_ERROR(500, "InternalError", "内部错误", "An internal error has occured, please try again later or contact service support."),
    INTERNAL_ERROR_ALGO(500, "InternalError.Algo", "内部算法错误", "An internal error has occured during execution, please try again later or contact service support."),
    SYSTEM_ERROR(500, "SystemError", "系统错误", "An system error has occured, please try again later."),
    INTERNAL_ERROR_TIMEOUT(500, "InternalError.Timeout", "异步任务从网关提交给算法服务层之后等待时间 3 小时，如果在这期间始终没有结果，则超时。", "An internal timeout error has occured during execution, please try again later or contact service support."),
    REWRITE_FAILED(500, "RewriteFailed", "调用改写prompt的大模型失败", "Failed to rewrite content for prompt."),
    RETRIEVAL_FAILED(500, "RetrivalFailed", "文档检索失败", "Failed to retrieve data from documents."),
    APP_PROCESS_FAILED(500, "AppProcessFailed", "应用流程处理失败", "Failed to proceed application request."),
    MODEL_SERVICE_FAILED(500, "ModelServiceFailed", "模型服务调用失败", "Failed to request model service."),
    INVOKE_PLUGIN_FAILED(500, "InvokePluginFailed", "插件调用失败", "Failed to invoke plugin."),
    MODEL_UNAVAILABLE(503, "ModelUnavailable", "模型暂时无法提供服务", "Model is unavailable, please try again later.");

    override fun toString(): String = "{\"statusCode\":$statusCode,\"code\":\"$code\",\"message\":\"$message\",\"messageEn\":\"$messageEn\"}"

    companion object {
        fun fromCodeAndStatusCode(
            code: String,
            statusCode: Int
        ): ResponseCode {
            return when {
                code == NETWORK_ERROR.code && statusCode == NETWORK_ERROR.statusCode -> NETWORK_ERROR
                code == RESPONSE_ERROR.code && statusCode == RESPONSE_ERROR.statusCode -> RESPONSE_ERROR
                code == INVALID_PARAMETER.code && statusCode == INVALID_PARAMETER.statusCode -> INVALID_PARAMETER
                code == DATA_INSPECTION_FAILED.code && statusCode == DATA_INSPECTION_FAILED.statusCode -> DATA_INSPECTION_FAILED
                code == BAD_REQUEST_EMPTY_INPUT.code && statusCode == BAD_REQUEST_EMPTY_INPUT.statusCode -> BAD_REQUEST_EMPTY_INPUT
                code == BAD_REQUEST_EMPTY_PARAMETERS.code && statusCode == BAD_REQUEST_EMPTY_PARAMETERS.statusCode -> BAD_REQUEST_EMPTY_PARAMETERS
                code == BAD_REQUEST_EMPTY_MODEL.code && statusCode == BAD_REQUEST_EMPTY_MODEL.statusCode -> BAD_REQUEST_EMPTY_MODEL
                code == INVALID_URL.code && statusCode == INVALID_URL.statusCode -> INVALID_URL
                code == ARREARAGE.code && statusCode == ARREARAGE.statusCode -> ARREARAGE
                code == UNSUPPORTED_OPERATION.code && statusCode == UNSUPPORTED_OPERATION.statusCode -> UNSUPPORTED_OPERATION
                code == FLOW_NOT_PUBLISHED.code && statusCode == FLOW_NOT_PUBLISHED.statusCode -> FLOW_NOT_PUBLISHED
                code == INVALID_SCHEMA.code && statusCode == INVALID_SCHEMA.statusCode -> INVALID_SCHEMA
                code == INVALID_SCHEMA_FORMAT.code && statusCode == INVALID_SCHEMA_FORMAT.statusCode -> INVALID_SCHEMA_FORMAT
                code == FAQ_RULE_BLOCKED.code && statusCode == FAQ_RULE_BLOCKED.statusCode -> FAQ_RULE_BLOCKED
                code == CUSTOM_ROLE_BLOCKED.code && statusCode == CUSTOM_ROLE_BLOCKED.statusCode -> CUSTOM_ROLE_BLOCKED
                code == INTERNAL_ERROR_ALGO_400.code && statusCode == INTERNAL_ERROR_ALGO_400.statusCode -> INTERNAL_ERROR_ALGO_400
                code == INVALID_API_KEY.code && statusCode == INVALID_API_KEY.statusCode -> INVALID_API_KEY
                code == ACCESS_DENIED.code && statusCode == ACCESS_DENIED.statusCode -> ACCESS_DENIED
                code == WORK_SPACE_ACCESS_DENIED.code && statusCode == WORK_SPACE_ACCESS_DENIED.statusCode -> WORK_SPACE_ACCESS_DENIED
                code == MODEL_ACCESS_DENIED.code && statusCode == MODEL_ACCESS_DENIED.statusCode -> MODEL_ACCESS_DENIED
                code == ACCESS_DENIED_UNPURCHASED.code && statusCode == ACCESS_DENIED_UNPURCHASED.statusCode -> ACCESS_DENIED_UNPURCHASED
                code == WORK_SPACE_NOT_FOUND.code && statusCode == WORK_SPACE_NOT_FOUND.statusCode -> WORK_SPACE_NOT_FOUND
                code == MODEL_NOT_FOUND.code && statusCode == MODEL_NOT_FOUND.statusCode -> MODEL_NOT_FOUND
                code == REQUEST_TIMEOUT.code && statusCode == REQUEST_TIMEOUT.statusCode -> REQUEST_TIMEOUT
                code == BAD_REQUEST_TOO_LARGE.code && statusCode == BAD_REQUEST_TOO_LARGE.statusCode -> BAD_REQUEST_TOO_LARGE
                code == BAD_REQUEST_INPUT_DOWNLOAD_FAILED.code && statusCode == BAD_REQUEST_INPUT_DOWNLOAD_FAILED.statusCode -> BAD_REQUEST_INPUT_DOWNLOAD_FAILED
                code == BAD_REQUEST_UNSUPPORTED_FILE_FORMAT.code && statusCode == BAD_REQUEST_UNSUPPORTED_FILE_FORMAT.statusCode -> BAD_REQUEST_UNSUPPORTED_FILE_FORMAT
                code == THROTTLING.code && statusCode == THROTTLING.statusCode -> THROTTLING
                code == THROTTLING_RATE_QUOTA.code && statusCode == THROTTLING_RATE_QUOTA.statusCode -> THROTTLING_RATE_QUOTA
                code == THROTTLING_ALLOCATION_QUOTA.code && statusCode == THROTTLING_ALLOCATION_QUOTA.statusCode -> THROTTLING_ALLOCATION_QUOTA
                code == LIMIT_REQUESTS.code && statusCode == LIMIT_REQUESTS.statusCode -> LIMIT_REQUESTS
                code == PREPAID_BILL_OVERDUE.code && statusCode == PREPAID_BILL_OVERDUE.statusCode -> PREPAID_BILL_OVERDUE
                code == POSTPAID_BILL_OVERDUE.code && statusCode == POSTPAID_BILL_OVERDUE.statusCode -> POSTPAID_BILL_OVERDUE
                code == COMMODITY_NOT_PURCHASED.code && statusCode == COMMODITY_NOT_PURCHASED.statusCode -> COMMODITY_NOT_PURCHASED
                code == INTERNAL_ERROR.code && statusCode == INTERNAL_ERROR.statusCode -> INTERNAL_ERROR
                code == INTERNAL_ERROR_ALGO.code && statusCode == INTERNAL_ERROR_ALGO.statusCode -> INTERNAL_ERROR_ALGO
                code == SYSTEM_ERROR.code && statusCode == SYSTEM_ERROR.statusCode -> SYSTEM_ERROR
                code == INTERNAL_ERROR_TIMEOUT.code && statusCode == INTERNAL_ERROR_TIMEOUT.statusCode -> INTERNAL_ERROR_TIMEOUT
                code == REWRITE_FAILED.code && statusCode == REWRITE_FAILED.statusCode -> REWRITE_FAILED
                code == RETRIEVAL_FAILED.code && statusCode == RETRIEVAL_FAILED.statusCode -> RETRIEVAL_FAILED
                code == APP_PROCESS_FAILED.code && statusCode == APP_PROCESS_FAILED.statusCode -> APP_PROCESS_FAILED
                code == MODEL_SERVICE_FAILED.code && statusCode == MODEL_SERVICE_FAILED.statusCode -> MODEL_SERVICE_FAILED
                code == INVOKE_PLUGIN_FAILED.code && statusCode == INVOKE_PLUGIN_FAILED.statusCode -> INVOKE_PLUGIN_FAILED
                code == MODEL_UNAVAILABLE.code && statusCode == MODEL_UNAVAILABLE.statusCode -> MODEL_UNAVAILABLE
                else -> UNKNOWN_ERROR
            }
        }
    }
}
