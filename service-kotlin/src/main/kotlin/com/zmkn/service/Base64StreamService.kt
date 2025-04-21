package com.zmkn.service

import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.apply

class Base64StreamService(outputFile: File) {
    constructor(outputFilePath: String) : this(
        File(outputFilePath).apply {
            parentFile.mkdirs()
        }
    )

    private val _decoder: Base64.Decoder = Base64.getDecoder()
    private val _outputStream = FileOutputStream(outputFile)

    fun write(chunk: String): Base64StreamService {
        val decoded = _decoder.decode(chunk)
        _outputStream.write(decoded)
        return this
    }

    fun close() {
        _outputStream.close()
    }
}
