package com.zmkn.module.aliyunoss

import com.aliyun.oss.OSSClientBuilder
import com.aliyun.oss.model.DeleteObjectsRequest
import com.aliyun.oss.model.ObjectMetadata
import com.zmkn.module.aliyunoss.extension.toClientConfiguration
import com.zmkn.module.aliyunoss.extension.toCredentialsProvider
import com.zmkn.module.aliyunoss.model.Config
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream

class AliyunOss(config: Config) {
    private val _bucketName = config.bucket
    private val _client = OSSClientBuilder.create()
        .endpoint(config.endpoint)
        .region(config.region)
        .credentialsProvider(config.toCredentialsProvider())
        .clientConfiguration(config.toClientConfiguration())
        .build()

    fun uploadSync(
        filePath: String,
        inputStream: InputStream,
        metadata: ObjectMetadata? = null,
    ) {
        _client.putObject(_bucketName, filePath, inputStream, metadata)
    }

    fun uploadSync(
        filePath: String,
        file: File,
        metadata: ObjectMetadata? = null,
    ) = uploadSync(filePath, file.inputStream(), metadata)

    fun downloadSync(filePath: String): InputStream {
        val ossObject = _client.getObject(_bucketName, filePath)
        return ossObject.objectContent
    }

    fun deleteSync(filePaths: List<String>): List<String> {
        val deleteObjectsResult = _client.deleteObjects(DeleteObjectsRequest(_bucketName).withKeys(filePaths).withEncodingType("url"))
        return deleteObjectsResult.deletedObjects
    }

    fun fileExistsSync(
        filePath: String,
        isOnlyInOSS: Boolean = false,
    ): Boolean {
        return _client.doesObjectExist(_bucketName, filePath, isOnlyInOSS)
    }

    suspend fun upload(
        filePath: String,
        inputStream: InputStream,
        metadata: ObjectMetadata? = null,
    ) = withContext(Dispatchers.IO) {
        uploadSync(filePath, inputStream, metadata)
    }

    suspend fun upload(
        filePath: String,
        file: File,
        metadata: ObjectMetadata? = null,
    ) = withContext(Dispatchers.IO) {
        uploadSync(filePath, file, metadata)
    }

    suspend fun download(filePath: String) = withContext(Dispatchers.IO) {
        downloadSync(filePath)
    }

    suspend fun delete(filePaths: List<String>) = withContext(Dispatchers.IO) {
        deleteSync(filePaths)
    }

    suspend fun delete(vararg filePaths: String) = delete(filePaths.toList())

    suspend fun fileExists(
        filePath: String,
        isOnlyInOSS: Boolean = false,
    ) = withContext(Dispatchers.IO) {
        fileExistsSync(filePath, isOnlyInOSS)
    }
}
