package com.zmkn.extension

import org.bson.Document

private fun documentListMerge(original: List<*>, vararg documentsList: List<*>): MutableList<Any?> {
    val newList = mutableListOf<Any?>()
    original.forEach { document ->
        val element = when (document) {
            is Document -> document.deepCopy()
            is List<*> -> documentListMerge(document)
            else -> document
        }
        newList.add(element)
    }
    if (documentsList.isNotEmpty()) {
        documentsList.forEach { documents ->
            documents.forEach { document ->
                if (!newList.contains(document)) {
                    newList.add(document)
                }
            }
        }
    }
    return newList
}

fun Document.deepCopy(): Document {
    val newDocument = Document()
    for ((key, value) in this) {
        when (value) {
            is Document -> {
                newDocument.append(key, value.deepCopy())
            }

            is List<*> -> {
                val newList = mutableListOf<Any?>()
                for (item in value) {
                    if (item is Document) {
                        newList.add(item.deepCopy())
                    } else {
                        newList.add(item)
                    }
                }
                newDocument.append(key, newList)
            }

            else -> newDocument.append(key, value)
        }
    }
    return newDocument
}

fun Document.filter(predicate: (property: Map.Entry<String, Any?>) -> Boolean): Document {
    val newDocument = Document()
    forEach {
        if (predicate(it)) {
            newDocument.append(it.key, it.value)
        }
    }
    return newDocument
}

fun Document.assign(vararg documents: Document): Document {
    val newDocument = deepCopy()
    if (documents.isNotEmpty()) {
        documents.forEach { document ->
            document.forEach { property ->
                val propertyValue = property.value
                if (propertyValue != null) {
                    val propertyName = property.key
                    val existingProperty = newDocument.get(propertyName)
                    if (propertyValue is Document) {
                        if (existingProperty != null && existingProperty is Document) {
                            newDocument[propertyName] = existingProperty.assign(propertyValue)
                        } else {
                            newDocument[propertyName] = propertyValue.deepCopy()
                        }
                    } else if (propertyValue is List<*>) {
                        if (existingProperty != null && existingProperty is List<*>) {
                            newDocument[propertyName] = documentListMerge(existingProperty, propertyValue)
                        } else {
                            newDocument[propertyName] = documentListMerge(propertyValue)
                        }
                    } else {
                        newDocument[propertyName] = propertyValue
                    }
                }
            }
        }
    }
    return newDocument
}
