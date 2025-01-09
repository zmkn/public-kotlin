package com.zmkn.module.kmongo.model

import kotlinx.serialization.Serializable

@Serializable
data class MultipleProjection<T>(val data: T)
