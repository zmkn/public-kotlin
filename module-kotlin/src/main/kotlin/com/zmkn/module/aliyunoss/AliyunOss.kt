package com.zmkn.module.aliyunoss

import com.aliyun.oss.OSSClientBuilder
import com.aliyun.oss.event.ProgressListener
import com.aliyun.oss.model.*
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

    fun shutdown() {
        _client.shutdown()
    }

    fun uploadSync(
        putObjectRequest: PutObjectRequest,
    ): PutObjectResult {
        return _client.putObject(putObjectRequest)
    }

    fun uploadSync(
        uploadFileRequest: UploadFileRequest,
    ): UploadFileResult {
        return _client.uploadFile(uploadFileRequest)
    }

    fun uploadSync(
        filePath: String,
        inputStream: InputStream,
        metadata: ObjectMetadata? = null,
        progressListener: ProgressListener? = null, // 设置进度条监听器。
    ): PutObjectResult {
        val putObjectRequest = PutObjectRequest(_bucketName, filePath, inputStream)
        if (metadata != null) {
            putObjectRequest.metadata = metadata
        }
        if (progressListener != null) {
            putObjectRequest.progressListener = progressListener
        }
        return uploadSync(putObjectRequest)
    }

    fun uploadSync(
        filePath: String,
        file: File,
        metadata: ObjectMetadata? = null,
        progressListener: ProgressListener? = null, // 设置进度条监听器。
    ): PutObjectResult {
        val putObjectRequest = PutObjectRequest(_bucketName, filePath, file)
        if (metadata != null) {
            putObjectRequest.metadata = metadata
        }
        if (progressListener != null) {
            putObjectRequest.progressListener = progressListener
        }
        return uploadSync(putObjectRequest)
    }

    fun uploadSync(
        filePath: String,
        localFilePath: String, // 本地文件的完整路径
        partSize: Long = 1024 * 100, // 指定上传的分片大小，单位为字节，取值范围为100 KB~5 GB。默认值为100 KB。
        taskNum: Int = 5, // 指定上传并发线程数，默认值为5。
        enableCheckpoint: Boolean = true, // 开启断点续传，默认开启。
        metadata: ObjectMetadata? = null,
        progressListener: ProgressListener? = null, // 设置进度条监听器。
    ): UploadFileResult {
        val uploadFileRequest = UploadFileRequest(_bucketName, filePath, localFilePath, partSize, taskNum, enableCheckpoint)
        if (metadata != null) {
            uploadFileRequest.objectMetadata = metadata
        }
        if (progressListener != null) {
            uploadFileRequest.progressListener = progressListener
        }
        return uploadSync(uploadFileRequest)
    }

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
        putObjectRequest: PutObjectRequest,
    ) = withContext(Dispatchers.IO) {
        uploadSync(putObjectRequest)
    }

    suspend fun upload(
        uploadFileRequest: UploadFileRequest,
    ) = withContext(Dispatchers.IO) {
        uploadSync(uploadFileRequest)
    }

    suspend fun upload(
        filePath: String,
        inputStream: InputStream,
        metadata: ObjectMetadata? = null,
        progressListener: ProgressListener? = null,
    ) = withContext(Dispatchers.IO) {
        uploadSync(filePath, inputStream, metadata, progressListener)
    }

    suspend fun upload(
        filePath: String,
        file: File,
        metadata: ObjectMetadata? = null,
        progressListener: ProgressListener? = null,
    ) = withContext(Dispatchers.IO) {
        uploadSync(filePath, file, metadata, progressListener)
    }

    suspend fun upload(
        filePath: String,
        localFilePath: String, // 本地文件的完整路径
        partSize: Long = 1024 * 100, // 指定上传的分片大小，单位为字节，取值范围为100 KB~5 GB。默认值为100 KB。
        taskNum: Int = 5, // 指定上传并发线程数，默认值为5。
        enableCheckpoint: Boolean = true, // 开启断点续传，默认开启。
        metadata: ObjectMetadata? = null,
        progressListener: ProgressListener? = null, // 设置进度条监听器。
    ) = withContext(Dispatchers.IO) {
        uploadSync(filePath, localFilePath, partSize, taskNum, enableCheckpoint, metadata, progressListener)
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
