package com.zmkn.util

import kotlin.random.Random

object RandomUtils {
    const val NUMBER_RANGES = "0123456789"
    const val LETTER_RANGES = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    const val DEFAULT_RANGES = "$NUMBER_RANGES$LETTER_RANGES"

    fun generateRandomString(
        length: Int,
        ranges: String,
    ): String {
        return if (length > 0) {
            if (ranges.isNotBlank()) {
                val sb = StringBuilder()
                (0 until length).forEach {
                    val randomIndex = Random.nextInt(ranges.length)
                    sb.append(ranges[randomIndex])
                }
                sb.toString()
            } else {
                throw IllegalArgumentException("Range must not be blank.")
            }
        } else {
            throw IllegalArgumentException("Length must be greater than 0.")
        }
    }

    fun generateRandomString(length: Int): String = generateRandomString(length, DEFAULT_RANGES)

    fun generateRandomNumber(length: Int): String = generateRandomString(length, NUMBER_RANGES)

    fun generateRandomLetter(length: Int): String = generateRandomString(length, LETTER_RANGES)
}
