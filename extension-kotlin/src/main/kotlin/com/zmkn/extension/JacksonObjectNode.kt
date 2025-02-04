package com.zmkn.extension

import com.fasterxml.jackson.databind.node.ObjectNode

fun ObjectNode.filter(predicate: (fieldName: String) -> Boolean): ObjectNode {
    val newObjectNode = deepCopy()
    fieldNames().asSequence().toList().forEach {
        if (!predicate(it)) {
            newObjectNode.remove(it)
        }
    }
    return newObjectNode
}
