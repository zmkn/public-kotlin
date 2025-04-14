package com.zmkn.util

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject

object JsonUtils {
    fun isLikelyJson(input: String): Boolean {
        val trimmed = input.trim()
        return if (trimmed.isEmpty()) {
            false
        } else {
            val first = trimmed.first()
            val last = trimmed.last()
            (first == '{' && last == '}') || (first == '[' && last == ']')
        }
    }

    fun isJsonValid(input: String): Boolean {
        return try {
            Json.parseToJsonElement(input)
            true
        } catch (_: Exception) {
            false
        }
    }

    fun isJsonValidAdvanced(input: String): Boolean {
        return isLikelyJson(input) && isJsonValid(input)
    }

    fun isObjectJson(jsonString: String): Boolean {
        return try {
            val element = Json.parseToJsonElement(jsonString)
            element is JsonObject
        } catch (_: Exception) {
            false
        }
    }

    fun isArrayJson(jsonString: String): Boolean {
        return try {
            val element = Json.parseToJsonElement(jsonString)
            element is JsonArray
        } catch (_: Exception) {
            false
        }
    }
}
