package com.zmkn.extension

import tools.jackson.databind.node.ArrayNode
import tools.jackson.databind.node.ObjectNode

fun ObjectNode.filter(predicate: (fieldName: String) -> Boolean): ObjectNode {
    val newObjectNode = deepCopy()
    propertyNames().toList().forEach {
        if (!predicate(it)) {
            newObjectNode.remove(it)
        }
    }
    return newObjectNode
}

fun ObjectNode.assign(vararg objectNodes: ObjectNode): ObjectNode {
    val newObjectNode = deepCopy()
    if (objectNodes.isNotEmpty()) {
        objectNodes.forEach { objectNode ->
            val properties = objectNode.properties()
            properties.forEach { property ->
                val propertyValue = property.value
                if (propertyValue != null) {
                    val propertyName = property.key
                    val existingProperty = newObjectNode.get(propertyName)
                    if (propertyValue.isObject) {
                        if (existingProperty != null && existingProperty.isObject) {
                            newObjectNode.set(propertyName, (existingProperty as ObjectNode).assign(propertyValue as ObjectNode))
                        } else {
                            newObjectNode.set(propertyName, propertyValue.deepCopy())
                        }
                    } else if (propertyValue.isArray) {
                        if (existingProperty != null && existingProperty.isArray) {
                            newObjectNode.set(propertyName, (existingProperty as ArrayNode).assign(propertyValue as ArrayNode))
                        } else {
                            newObjectNode.set(propertyName, propertyValue.deepCopy())
                        }
                    } else {
                        newObjectNode.set(propertyName, propertyValue.deepCopy())
                    }
                }
            }
        }
    }
    return newObjectNode
}
