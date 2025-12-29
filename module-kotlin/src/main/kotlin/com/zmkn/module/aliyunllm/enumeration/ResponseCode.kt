package com.zmkn.module.aliyunllm.enumeration

enum class ResponseCode(
    val statusCode: Int,
    val code: String,
    val message: String,
    val messageEn: String,
) {
    UNKNOWN_ERROR(0, "unknown_error", "未知异常", "Unknown Error."),
    NETWORK_ERROR(-1, "network error", "网络异常", "Network error."),
    RESPONSE_ERROR(400, "response_error", "响应异常", "Response error."),
    INVALID_PARAMETER(
        400,
        "InvalidParameter",
        "接口调用参数不合法",
        "Required parameter(s) missing or invalid, please check the request parameters."
    ),
    DATA_INSPECTION_FAILED(
        400,
        "DataInspectionFailed",
        "数据检查错误，输入或者输出包含疑似敏感内容被绿网拦截。",
        "Input or output data may contain inappropriate content. Input data may contain inappropriate content. Output data may contain inappropriate content."
    ),
    BAD_REQUEST_EMPTY_INPUT(
        400,
        "BadRequest.EmptyInput",
        "请求的输入不能为空",
        "Required input parameter missing from request."
    ),
    BAD_REQUEST_EMPTY_PARAMETERS(
        400,
        "BadRequest.EmptyParameters",
        "请求的参数不能为空",
        "Required parameter 'parameters' missing from request."
    ),
    BAD_REQUEST_EMPTY_MODEL(
        400,
        "BadRequest.EmptyModel",
        "请求输入的模型不能为空",
        "Required parameter 'model' missing from request."
    ),
    BAD_REQUEST_RESOURCE_NOT_EXIST(
        400,
        "BadRequest.ResourceNotExist",
        "请求的资源不存在",
        "The Required resource not exist."
    ),
    INVALID_URL(400, "InvalidURL", "请求的URL错误", "Invalid URL provided in your request."),
    ARREARAGE(
        400,
        "Arrearage",
        "账户异常，访问被拒绝。",
        "Access denied, please make sure your account is in good standing."
    ),
    UNSUPPORTED_OPERATION(
        400,
        "UnsupportedOperation",
        "关联的对象不支持该操作",
        "The operation is unsupported on the referee object."
    ),
    FLOW_NOT_PUBLISHED(
        400,
        "FlowNotPublished",
        "流程未发布，请发布流程后再重试。",
        "Flow has not published yet, please publish flow and try again."
    ),
    INVALID_SCHEMA(400, "InvalidSchema", "请输入数据库Schema信息", "Database schema is invalid for text2sql."),
    INVALID_SCHEMA_FORMAT(
        400,
        "InvalidSchemaFormat",
        "输入数据表信息格式异常",
        "Database schema format is invalid for text2sql."
    ),
    FAQ_RULE_BLOCKED(400, "FaqRuleBlocked", "命中FAQ规则干预模块", "Input or output data is blocked by faq rule."),
    CUSTOM_ROLE_BLOCKED(
        400,
        "CustomRoleBlocked",
        "请求或响应内容没有通过自定义策略",
        "Input or output data may contain inappropriate content with custom rule."
    ),
    INTERNAL_ERROR_ALGO_400(
        400,
        "InternalError.Algo",
        "URL请求的响应头信息缺失Content-Length字段",
        "Missing Content-Length of multimodal url."
    ),
    INVALID_API_KEY(401, "InvalidApiKey", "请求中的 ApiKey 错误", "Invalid API-key provided."),
    ACCESS_DENIED(403, "AccessDenied", "无权访问此 API", "Access denied."),
    WORK_SPACE_ACCESS_DENIED(
        403,
        "Workspace.AccessDenied",
        "无权限访问业务空间的应用或者模型",
        "Workspace access denied."
    ),
    MODEL_ACCESS_DENIED(403, "Model.AccessDenied", "子账号无权限访问业务空间的模型", "Model access denied."),
    ACCESS_DENIED_UNPURCHASED(
        403,
        "AccessDenied.Unpurchased",
        "账户异常，访问模型被拒绝。",
        "Access to model denied. Please make sure you are eligible for using the model."
    ),
    WORK_SPACE_NOT_FOUND(404, "WorkSpaceNotFound", "用户空间信息不存在", "WorkSpace can not be found."),
    MODEL_NOT_FOUND(404, "ModelNotFound", "当前访问的模型不存在", "Model can not be found."),
    REQUEST_TIMEOUT(
        408,
        "RequestTimeOut",
        "请求超时，您可尝试再次发起请求。",
        "Request timed out, please try again later."
    ),
    BAD_REQUEST_TOO_LARGE(413, "BadRequest.TooLarge", "接入层网关返回请求体过大", "Payload Too Large."),
    BAD_REQUEST_INPUT_DOWNLOAD_FAILED(
        415,
        "BadRequest.InputDownloadFailed",
        "下载输入文件失败",
        "Failed to download the input file: xxx."
    ),
    BAD_REQUEST_UNSUPPORTED_FILE_FORMAT(
        415,
        "BadRequest.UnsupportedFileFormat",
        "输入文件的格式不受支持",
        "Input file format is not supported."
    ),
    THROTTLING(429, "Throttling", "接口调用触发限流", "Requests throttling triggered."),
    THROTTLING_RATE_QUOTA(
        429,
        "Throttling.RateQuota",
        "调用频次触发限流，比如每秒钟请求次数。",
        "Requests rate limit exceeded, please try again later."
    ),
    THROTTLING_ALLOCATION_QUOTA(
        429,
        "Throttling.AllocationQuota",
        "一段时间调用量触发限流，比如每分钟生成Token数。",
        "Allocated quota exceeded, please increase your quota limit."
    ),
    LIMIT_REQUESTS(
        429,
        "LimitRequests",
        "超出调用限制，您需等到不满足限流条件时才能再次调用。",
        "You exceeded your current requests list."
    ),
    PREPAID_BILL_OVERDUE(429, "PrepaidBillOverdue", "业务空间预付费账单到期", "The prepaid bill is overdue."),
    POSTPAID_BILL_OVERDUE(429, "PostpaidBillOverdue", "模型推理商品已失效", "The postpaid bill is overdue."),
    COMMODITY_NOT_PURCHASED(429, "CommodityNotPurchased", "业务空间未订购", "Commodity has not purchased yet."),
    INTERNAL_ERROR(
        500,
        "InternalError",
        "内部错误",
        "An internal error has occured, please try again later or contact service support."
    ),
    INTERNAL_ERROR_ALGO(
        500,
        "InternalError.Algo",
        "内部算法错误",
        "An internal error has occured during execution, please try again later or contact service support."
    ),
    SYSTEM_ERROR(500, "SystemError", "系统错误", "An system error has occured, please try again later."),
    INTERNAL_ERROR_TIMEOUT(
        500,
        "InternalError.Timeout",
        "异步任务从网关提交给算法服务层之后等待时间 3 小时，如果在这期间始终没有结果，则超时。",
        "An internal timeout error has occured during execution, please try again later or contact service support."
    ),
    REWRITE_FAILED(500, "RewriteFailed", "调用改写prompt的大模型失败", "Failed to rewrite content for prompt."),
    RETRIEVAL_FAILED(500, "RetrivalFailed", "文档检索失败", "Failed to retrieve data from documents."),
    APP_PROCESS_FAILED(500, "AppProcessFailed", "应用流程处理失败", "Failed to proceed application request."),
    MODEL_SERVICE_FAILED(500, "ModelServiceFailed", "模型服务调用失败", "Failed to request model service."),
    INVOKE_PLUGIN_FAILED(500, "InvokePluginFailed", "插件调用失败", "Failed to invoke plugin."),
    MODEL_UNAVAILABLE(503, "ModelUnavailable", "模型暂时无法提供服务", "Model is unavailable, please try again later.");

    override fun toString(): String = name

    companion object {
        fun fromCodeAndStatusCode(
            code: String,
            statusCode: Int
        ): ResponseCode = when (code) {
            NETWORK_ERROR.code if statusCode == NETWORK_ERROR.statusCode -> NETWORK_ERROR
            RESPONSE_ERROR.code if statusCode == RESPONSE_ERROR.statusCode -> RESPONSE_ERROR
            INVALID_PARAMETER.code if statusCode == INVALID_PARAMETER.statusCode -> INVALID_PARAMETER
            DATA_INSPECTION_FAILED.code if statusCode == DATA_INSPECTION_FAILED.statusCode -> DATA_INSPECTION_FAILED
            BAD_REQUEST_EMPTY_INPUT.code if statusCode == BAD_REQUEST_EMPTY_INPUT.statusCode -> BAD_REQUEST_EMPTY_INPUT
            BAD_REQUEST_EMPTY_PARAMETERS.code if statusCode == BAD_REQUEST_EMPTY_PARAMETERS.statusCode -> BAD_REQUEST_EMPTY_PARAMETERS
            BAD_REQUEST_EMPTY_MODEL.code if statusCode == BAD_REQUEST_EMPTY_MODEL.statusCode -> BAD_REQUEST_EMPTY_MODEL
            BAD_REQUEST_RESOURCE_NOT_EXIST.code if statusCode == BAD_REQUEST_RESOURCE_NOT_EXIST.statusCode -> BAD_REQUEST_RESOURCE_NOT_EXIST
            INVALID_URL.code if statusCode == INVALID_URL.statusCode -> INVALID_URL
            ARREARAGE.code if statusCode == ARREARAGE.statusCode -> ARREARAGE
            UNSUPPORTED_OPERATION.code if statusCode == UNSUPPORTED_OPERATION.statusCode -> UNSUPPORTED_OPERATION
            FLOW_NOT_PUBLISHED.code if statusCode == FLOW_NOT_PUBLISHED.statusCode -> FLOW_NOT_PUBLISHED
            INVALID_SCHEMA.code if statusCode == INVALID_SCHEMA.statusCode -> INVALID_SCHEMA
            INVALID_SCHEMA_FORMAT.code if statusCode == INVALID_SCHEMA_FORMAT.statusCode -> INVALID_SCHEMA_FORMAT
            FAQ_RULE_BLOCKED.code if statusCode == FAQ_RULE_BLOCKED.statusCode -> FAQ_RULE_BLOCKED
            CUSTOM_ROLE_BLOCKED.code if statusCode == CUSTOM_ROLE_BLOCKED.statusCode -> CUSTOM_ROLE_BLOCKED
            INTERNAL_ERROR_ALGO_400.code if statusCode == INTERNAL_ERROR_ALGO_400.statusCode -> INTERNAL_ERROR_ALGO_400
            INVALID_API_KEY.code if statusCode == INVALID_API_KEY.statusCode -> INVALID_API_KEY
            ACCESS_DENIED.code if statusCode == ACCESS_DENIED.statusCode -> ACCESS_DENIED
            WORK_SPACE_ACCESS_DENIED.code if statusCode == WORK_SPACE_ACCESS_DENIED.statusCode -> WORK_SPACE_ACCESS_DENIED
            MODEL_ACCESS_DENIED.code if statusCode == MODEL_ACCESS_DENIED.statusCode -> MODEL_ACCESS_DENIED
            ACCESS_DENIED_UNPURCHASED.code if statusCode == ACCESS_DENIED_UNPURCHASED.statusCode -> ACCESS_DENIED_UNPURCHASED
            WORK_SPACE_NOT_FOUND.code if statusCode == WORK_SPACE_NOT_FOUND.statusCode -> WORK_SPACE_NOT_FOUND
            MODEL_NOT_FOUND.code if statusCode == MODEL_NOT_FOUND.statusCode -> MODEL_NOT_FOUND
            REQUEST_TIMEOUT.code if statusCode == REQUEST_TIMEOUT.statusCode -> REQUEST_TIMEOUT
            BAD_REQUEST_TOO_LARGE.code if statusCode == BAD_REQUEST_TOO_LARGE.statusCode -> BAD_REQUEST_TOO_LARGE
            BAD_REQUEST_INPUT_DOWNLOAD_FAILED.code if statusCode == BAD_REQUEST_INPUT_DOWNLOAD_FAILED.statusCode -> BAD_REQUEST_INPUT_DOWNLOAD_FAILED
            BAD_REQUEST_UNSUPPORTED_FILE_FORMAT.code if statusCode == BAD_REQUEST_UNSUPPORTED_FILE_FORMAT.statusCode -> BAD_REQUEST_UNSUPPORTED_FILE_FORMAT
            THROTTLING.code if statusCode == THROTTLING.statusCode -> THROTTLING
            THROTTLING_RATE_QUOTA.code if statusCode == THROTTLING_RATE_QUOTA.statusCode -> THROTTLING_RATE_QUOTA
            THROTTLING_ALLOCATION_QUOTA.code if statusCode == THROTTLING_ALLOCATION_QUOTA.statusCode -> THROTTLING_ALLOCATION_QUOTA
            LIMIT_REQUESTS.code if statusCode == LIMIT_REQUESTS.statusCode -> LIMIT_REQUESTS
            PREPAID_BILL_OVERDUE.code if statusCode == PREPAID_BILL_OVERDUE.statusCode -> PREPAID_BILL_OVERDUE
            POSTPAID_BILL_OVERDUE.code if statusCode == POSTPAID_BILL_OVERDUE.statusCode -> POSTPAID_BILL_OVERDUE
            COMMODITY_NOT_PURCHASED.code if statusCode == COMMODITY_NOT_PURCHASED.statusCode -> COMMODITY_NOT_PURCHASED
            INTERNAL_ERROR.code if statusCode == INTERNAL_ERROR.statusCode -> INTERNAL_ERROR
            INTERNAL_ERROR_ALGO.code if statusCode == INTERNAL_ERROR_ALGO.statusCode -> INTERNAL_ERROR_ALGO
            SYSTEM_ERROR.code if statusCode == SYSTEM_ERROR.statusCode -> SYSTEM_ERROR
            INTERNAL_ERROR_TIMEOUT.code if statusCode == INTERNAL_ERROR_TIMEOUT.statusCode -> INTERNAL_ERROR_TIMEOUT
            REWRITE_FAILED.code if statusCode == REWRITE_FAILED.statusCode -> REWRITE_FAILED
            RETRIEVAL_FAILED.code if statusCode == RETRIEVAL_FAILED.statusCode -> RETRIEVAL_FAILED
            APP_PROCESS_FAILED.code if statusCode == APP_PROCESS_FAILED.statusCode -> APP_PROCESS_FAILED
            MODEL_SERVICE_FAILED.code if statusCode == MODEL_SERVICE_FAILED.statusCode -> MODEL_SERVICE_FAILED
            INVOKE_PLUGIN_FAILED.code if statusCode == INVOKE_PLUGIN_FAILED.statusCode -> INVOKE_PLUGIN_FAILED
            MODEL_UNAVAILABLE.code if statusCode == MODEL_UNAVAILABLE.statusCode -> MODEL_UNAVAILABLE
            else -> UNKNOWN_ERROR
        }
    }
}
