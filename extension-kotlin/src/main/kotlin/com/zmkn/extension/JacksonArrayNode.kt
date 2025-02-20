package com.zmkn.extension

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode

fun ArrayNode.replace(index: Int, value: JsonNode): ArrayNode {
    if (index >= size()) {
        insert(index, value)
    } else {
        set(index, value)
    }
    return this
}

fun ArrayNode.assign(vararg arrayNodes: ArrayNode): ArrayNode {
    val newArrayNode = deepCopy()
    if (arrayNodes.isNotEmpty()) {
        arrayNodes.forEach { arrayNode ->
            for (index in 0 until arrayNode.size()) {
                val propertyValue = arrayNode.get(index)
                if (propertyValue != null) {
                    val existingProperty = newArrayNode.get(index)
                    if (propertyValue.isObject) {
                        if (existingProperty != null && existingProperty.isObject) {
                            newArrayNode.replace(index, (existingProperty as ObjectNode).assign(propertyValue as ObjectNode))
                        } else {
                            newArrayNode.replace(index, propertyValue.deepCopy())
                        }
                    } else if (propertyValue.isArray) {
                        if (existingProperty != null && existingProperty.isArray) {
                            newArrayNode.replace(index, (existingProperty as ArrayNode).assign(propertyValue as ArrayNode))
                        } else {
                            newArrayNode.replace(index, propertyValue.deepCopy())
                        }
                    } else {
                        newArrayNode.replace(index, propertyValue.deepCopy())
                    }
                }
            }
        }
    }
    return newArrayNode
}
