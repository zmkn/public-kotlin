package com.zmkn.module.aliyunllm.aigc.extension

import com.alibaba.dashscope.aigc.generation.TranslationOptions
import com.alibaba.dashscope.common.SearchOptions
import com.alibaba.dashscope.tools.ToolCallFunction
import com.zmkn.module.aliyunllm.aigc.model.GenerationParamOptions

fun GenerationParamOptions.Message.ToolCall.toToolCallBase() = ToolCallFunction().also {
    it.id = id
    it.type = type
    it.function = function.toToolCallBaseFunction()
}

fun GenerationParamOptions.Message.ToolCall.Function.toToolCallBaseFunction() = ToolCallFunction().CallFunction().also {
    it.name = name
    it.arguments = arguments
    it.output = output
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
        searchOptions.searchStrategy(it.value)
    }
}.build()

fun GenerationParamOptions.TranslationOptions.toTranslationOptions(): TranslationOptions = TranslationOptions.builder().also { translationOptions ->
    translationOptions.sourceLang(sourceLang)
    translationOptions.targetLang(targetLang)
    domains?.also {
        translationOptions.domains(it)
    }
    terms?.also { terms ->
        translationOptions.terms(terms.map {
            it.toTerm()
        })
    }
    tmList?.also { tmList ->
        translationOptions.tmList(tmList.map {
            it.toTm()
        })
    }
}.build()

fun GenerationParamOptions.TranslationOptions.Tm.toTm(): TranslationOptions.Tm = TranslationOptions.Tm.builder().also { tm ->
    tm.source(source)
    tm.target(target)
}.build()

fun GenerationParamOptions.TranslationOptions.Term.toTerm(): TranslationOptions.Term = TranslationOptions.Term.builder().also { term ->
    term.source(source)
    term.target(target)
}.build()
