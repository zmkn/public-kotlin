package com.zmkn.module.kmongo.extension

import org.bson.Document

fun Document.copy(): Document {
    val newDocument = Document()
    for ((key, value) in this) {
        when (value) {
            is Document -> {
                newDocument.append(key, value.copy())
            }

            is List<*> -> {
                val newList = mutableListOf<Any?>()
                for (item in value) {
                    if (item is Document) {
                        newList.add(item.copy())
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
