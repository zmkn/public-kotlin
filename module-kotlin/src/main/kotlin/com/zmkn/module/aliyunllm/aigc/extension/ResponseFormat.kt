package com.zmkn.module.aliyunllm.aigc.extension

import com.zmkn.module.aliyunllm.aigc.model.ResponseFormat
import com.alibaba.dashscope.common.ResponseFormat as DashscopeResponseFormat

fun ResponseFormat.toResponseFormat(): DashscopeResponseFormat = DashscopeResponseFormat.from(type.value)
