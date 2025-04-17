package com.zmkn.module.aliyunllm.aigc.extension

import com.alibaba.dashscope.aigc.generation.SearchInfo
import com.zmkn.module.aliyunllm.aigc.model.ResponseMessage

fun SearchInfo.toResponseMessageOutputSearchInfo(): ResponseMessage.Output.SearchInfo = ResponseMessage.Output.SearchInfo(
    searchResults = searchResults.mapNotNull {
        it.toResponseMessageOutputSearchInfoSearchResult()
    }
)

fun SearchInfo.SearchResult.toResponseMessageOutputSearchInfoSearchResult(): ResponseMessage.Output.SearchInfo.SearchResult = ResponseMessage.Output.SearchInfo.SearchResult(
    index = index,
    siteName = siteName,
    icon = icon,
    title = title,
    url = url,
)
