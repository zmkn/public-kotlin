package com.zmkn.extension

fun <E> MutableList<E>.addWithNegativeIndex(index: Int, element: E) {
    val adjustedIndex = when {
        index < 0 -> size + index + 1
        else -> index
    }

    // 确保调整后的索引在合法范围内
    require(adjustedIndex in 0..size) { "Index $index out of bounds for list size $size." }
    add(adjustedIndex, element)
}
