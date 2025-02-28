package com.zmkn.util

import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

object FileUtils {
    class Hold {
        private val _fileBytes: ByteArray

        constructor(fileInputStream: InputStream) {
            _fileBytes = fileInputStream.readBytes()
        }

        constructor(file: File) : this(FileInputStream(file))

        constructor(fileAbsolutePath: String) : this(FileInputStream(fileAbsolutePath))

        fun createByteArrayInputStream(): ByteArrayInputStream {
            return ByteArrayInputStream(_fileBytes)
        }

        fun createFile(fileAbsolutePath: String): File {
            File(fileAbsolutePath).writeBytes(_fileBytes)
            return File(fileAbsolutePath)
        }

        fun createFileInputStream(fileAbsolutePath: String): FileInputStream {
            return createFile(fileAbsolutePath).inputStream()
        }
    }

    fun getProjectRootDirectory(vararg paths: String): String {
        val projectRootDirectory = System.getProperty("ProjectRootDirectory")
        return if (projectRootDirectory == null) {
            val path = if (paths.isEmpty()) {
                ""
            } else {
                "./${paths.joinToString("/")}"
            }
            File(path).canonicalPath
        } else {
            projectRootDirectory
        }
    }

    fun copy(source: InputStream, vararg destinations: File) {
        val fileBytes: ByteArray = source.readBytes()
        if (destinations.isEmpty()) {
            throw IllegalArgumentException("destinations must not be empty")
        } else {
            destinations.forEach { destination ->
                destination.writeBytes(fileBytes)
            }
        }
    }

    fun copy(source: File, vararg destinations: File) {
        copy(FileInputStream(source), *destinations)
    }

    fun copy(source: String, vararg destinations: File) {
        copy(FileInputStream(source), *destinations)
    }

    fun copy(source: InputStream, vararg destinations: String) {
        if (destinations.isEmpty()) {
            throw IllegalArgumentException("destinations must not be empty")
        } else {
            val destinationFiles = destinations.map { destination ->
                val destinationFile = File(destination)
                destinationFile.parentFile?.mkdirs()
                destinationFile
            }
            copy(source, *destinationFiles.toTypedArray())
        }
    }

    fun copy(source: File, vararg destinations: String) {
        copy(FileInputStream(source), *destinations)
    }

    fun copy(source: String, vararg destinations: String) {
        copy(FileInputStream(source), *destinations)
    }

    fun exists(file: File): Boolean {
        return file.exists()
    }

    fun exists(absolutePath: String): Boolean {
        return exists(File(absolutePath))
    }
}
