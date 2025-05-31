package com.zmkn.util

import org.bson.BsonDocument
import org.bson.BsonInt32
import org.bson.BsonValue
import org.bson.conversions.Bson

object BsonUtils {
    /**
     * 反转单个排序方向
     */
    private fun reverseSortDirection(value: BsonValue): BsonValue {
        return when (value.asInt32().value) {
            1 -> BsonInt32(-1)
            -1 -> BsonInt32(1)
            else -> value
        }
    }

    /**
     * 反转排序方向
     * @param sort 原始排序条件
     * @return 反转后的排序条件
     */
    fun reverseSort(sort: Bson): Bson {
        val sortDoc = sort.toBsonDocument()
        val reversed = BsonDocument()
        sortDoc.forEach { (key, value) ->
            reversed.append(key, reverseSortDirection(value))
        }
        return reversed
    }
}
