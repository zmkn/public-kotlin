package com.zmkn.module.emqx.model

import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginationMeta(
    val page: Int = 1,
    val limit: Int = 100,
    val count: Int = 0,
    @SerialName("hasnext")
    @JsonProperty("hasnext")
    val hasNext: Boolean,
)
