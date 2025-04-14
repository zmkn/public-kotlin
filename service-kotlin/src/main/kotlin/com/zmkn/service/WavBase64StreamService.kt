package com.zmkn.service

import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile
import java.util.*
import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import kotlin.apply

class WavBase64StreamService(
    private val outputFile: File,
    private val audioFormat: AudioFormat,
) {
    constructor(
        outputFilePath: String,
        audioFormat: AudioFormat,
    ) : this(File(outputFilePath).apply {
        parentFile.mkdirs()
    }, audioFormat)

    constructor(outputFile: File) : this(outputFile, defaultAudioFormat)

    constructor(outputFilePath: String) : this(outputFilePath, defaultAudioFormat)

    private var _headerWritten: Boolean = false

    private val _decoder: Base64.Decoder = Base64.getDecoder()
    private val _outputStream = FileOutputStream(outputFile)

    fun write(chunk: String): WavBase64StreamService {
        val audioBytes = _decoder.decode(chunk)
        if (_headerWritten) {
            _outputStream.write(audioBytes)
        } else {
            _headerWritten = true
            val audioInputStream = AudioInputStream(
                ByteArrayInputStream(audioBytes),
                audioFormat,
                audioBytes.size.toLong(),
            )
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, _outputStream)
        }
        return this
    }

    fun close() {
        _outputStream.close()
        val randomAccessFile = RandomAccessFile(outputFile, "rw")
        val totalLength = randomAccessFile.length().toInt()
        if (totalLength > 36) {
            randomAccessFile.seek(4)
            randomAccessFile.writeInt(totalLength)
            randomAccessFile.seek(40)
            randomAccessFile.writeInt(totalLength - 36)
            randomAccessFile.close()
        }
    }

    companion object {
        val defaultAudioFormat = AudioFormat(
            24000f,     // 采样率
            16,         // 采样位数
            1,          // 声道数
            true,       // 是否带符号
            false       // 是否小端编码
        )
    }
}
