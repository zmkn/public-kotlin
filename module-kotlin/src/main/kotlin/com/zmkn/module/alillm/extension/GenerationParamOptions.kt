package com.zmkn.module.alillm.extension

import com.alibaba.dashscope.aigc.generation.SearchOptions
import com.alibaba.dashscope.tools.ToolCallFunction
import com.zmkn.module.alillm.model.GenerationParamOptions

fun GenerationParamOptions.Message.ToolCall.toToolCallBase() = ToolCallFunction().also {
    it.id = id
    it.type = type
    it.function = function.toToolCallBaseFunction()
}

fun GenerationParamOptions.Message.ToolCall.Function.toToolCallBaseFunction() = ToolCallFunction().CallFunction().also {
    it.name = name
    it.arguments = arguments
}

fun GenerationParamOptions.SearchOptions.toSearchOptions(): SearchOptions = SearchOptions.builder().also { searchOptions ->
    enableSource?.also {
        searchOptions.enableSource(it)
    }
    enableCitation?.also {
        searchOptions.enableCitation(it)
    }
    citationFormat?.also {
        searchOptions.citationFormat(it)
    }
    forcedSearch?.also {
        searchOptions.forcedSearch(it)
    }
    searchStrategy?.also {
        searchOptions.searchStrategy(it)
    }
}.build()
