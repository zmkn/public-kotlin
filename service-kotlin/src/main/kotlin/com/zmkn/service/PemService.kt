package com.zmkn.service

import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers
import org.bouncycastle.cert.X509CertificateHolder
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.jce.provider.BouncyCastleProvider.PROVIDER_NAME
import org.bouncycastle.openssl.PEMKeyPair
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.bouncycastle.util.io.pem.PemReader
import java.io.*
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Security

class PemService {
    private val _pemFileBytes: ByteArray

    constructor(pemFileAbsolutePath: String) : this(File(pemFileAbsolutePath))

    constructor(pemFile: File) {
        _pemFileBytes = pemFile.readBytes()
    }

    constructor(pemInputStream: InputStream) {
        pemInputStream.use {
            _pemFileBytes = it.readBytes()
        }
    }

    private fun getInputStreamReader(): InputStreamReader {
        val pemInputStream = ByteArrayInputStream(_pemFileBytes)
        return InputStreamReader(pemInputStream)
    }

    private fun getPemReader(): PemReader {
        return PemReader(getInputStreamReader())
    }

    private fun getType(): PemType {
        val pemReader = getPemReader()
        val pemObject = pemReader.readPemObject()
        val type = pemObject.type
        pemReader.close()
        return if (PemType.PUBLIC.value in type) {
            PemType.PUBLIC
        } else if (PemType.PRIVATE.value in type) {
            PemType.PRIVATE
        } else if (PemType.CERTIFICATE.value in type) {
            PemType.CERTIFICATE
        } else {
            PemType.UNKNOWN
        }
    }

    private fun getPkcs1PublicKey(): PublicKey {
        val pemReader = getPemReader()
        val pemParser = PEMParser(pemReader)
        val pemKeyPair = pemParser.readObject() as? PEMKeyPair
        pemParser.close()
        return pemKeyPair?.run {
            JcaPEMKeyConverter().setProvider(PROVIDER_NAME).getPublicKey(publicKeyInfo)
        } ?: throw IOException("PEM file type is not PKCS#1 public key type.")
    }

    private fun getPkcs8PublicKey(): PublicKey {
        val pemReader = getPemReader()
        val pemParser = PEMParser(pemReader)
        val publicKeyInfo = pemParser.readObject() as? SubjectPublicKeyInfo
        pemParser.close()
        return publicKeyInfo?.let {
            JcaPEMKeyConverter().setProvider(PROVIDER_NAME).getPublicKey(it)
        } ?: throw IOException("PEM file type is not PKCS#8 public key type.")
    }

    private fun getPkcs1PrivateKey(): PrivateKey {
        val pemReader = getPemReader()
        val pemParser = PEMParser(pemReader)
        val pemKeyPair = pemParser.readObject() as? PEMKeyPair
        pemParser.close()
        return pemKeyPair?.run {
            JcaPEMKeyConverter().setProvider(PROVIDER_NAME).getPrivateKey(privateKeyInfo)
        } ?: throw IOException("PEM file type is not PKCS#1 private key type.")
    }

    private fun getPkcs8PrivateKey(): PrivateKey {
        val pemReader = getPemReader()
        val pemParser = PEMParser(pemReader)
        val privateKeyInfo = pemParser.readObject() as? PrivateKeyInfo
        pemParser.close()
        return privateKeyInfo?.let {
            JcaPEMKeyConverter().setProvider(PROVIDER_NAME).getPrivateKey(it)
        } ?: throw IOException("PEM file type is not PKCS#8 private key type.")
    }

    private fun getCertificatePublicKey(): PublicKey {
        val pemReader = getPemReader()
        val pemParser = PEMParser(pemReader)
        val certificateHolder = pemParser.readObject() as? X509CertificateHolder
        pemParser.close()
        return certificateHolder?.run {
            JcaPEMKeyConverter().setProvider(PROVIDER_NAME).getPublicKey(subjectPublicKeyInfo)
        } ?: throw IOException("PEM file type is not certificates type.")
    }

    fun getInfo(): PemInfo {
        val pemReader = getPemReader()
        val pemParser = PEMParser(pemReader)
        val pemObject = pemParser.readObject()
        val pemType: PemType
        val pemFormat: PemFormat
        val pemAlgorithm: PemAlgorithm
        val algorithmId: String?
        pemParser.close()
        when (pemObject) {
            is SubjectPublicKeyInfo -> {
                pemType = PemType.PUBLIC
                pemFormat = PemFormat.PKCS8
                algorithmId = pemObject.algorithm.algorithm.id
            }

            is PrivateKeyInfo -> {
                pemType = PemType.PRIVATE
                pemFormat = PemFormat.PKCS8
                algorithmId = pemObject.privateKeyAlgorithm.algorithm.id
            }

            is PEMKeyPair -> {
                pemFormat = PemFormat.PKCS1
                algorithmId = pemObject.publicKeyInfo.algorithm.algorithm.id
                pemType = getType()
            }

            is X509CertificateHolder -> {
                pemType = PemType.CERTIFICATE
                pemFormat = PemFormat.X509
                algorithmId = pemObject.subjectPublicKeyInfo.algorithm.algorithm.id
            }

            else -> {
                pemType = PemType.UNKNOWN
                pemFormat = PemFormat.UNKNOWN
                algorithmId = null
            }
        }
        pemAlgorithm = when (algorithmId) {
            PKCSObjectIdentifiers.rsaEncryption.id -> {
                PemAlgorithm.RSA
            }

            X9ObjectIdentifiers.id_ecPublicKey.id -> {
                PemAlgorithm.EC
            }

            else -> {
                PemAlgorithm.UNKNOWN
            }
        }
        return PemInfo(
            type = pemType,
            format = pemFormat,
            algorithm = pemAlgorithm,
        )
    }

    fun getPublicKey(): PublicKey {
        val info = getInfo()
        return when (info.format) {
            PemFormat.PKCS1 -> {
                getPkcs1PublicKey()
            }

            PemFormat.PKCS8 -> {
                getPkcs8PublicKey()
            }

            PemFormat.X509 -> {
                getCertificatePublicKey()
            }

            else -> {
                throw IOException("PEM file type is not PKCS#1 or PKCS#8 public key type.")
            }
        }
    }

    fun getPrivateKey(): PrivateKey {
        val info = getInfo()
        return when (info.format) {
            PemFormat.PKCS1 -> {
                getPkcs1PrivateKey()
            }

            PemFormat.PKCS8 -> {
                getPkcs8PrivateKey()
            }

            else -> {
                throw IOException("PEM file type is not PKCS#1 or PKCS#8 private key type.")
            }
        }
    }

    companion object {
        init {
            Security.addProvider(BouncyCastleProvider())
        }
    }

    enum class PemType(val value: String) {
        UNKNOWN("UNKNOWN"),
        CERTIFICATE("CERTIFICATE"),
        PUBLIC("PUBLIC"),
        PRIVATE("PRIVATE");

        override fun toString(): String {
            return value
        }
    }

    enum class PemFormat(val value: String) {
        UNKNOWN("UNKNOWN"),
        X509("X509"),
        PKCS1("PKCS1"),
        PKCS8("PKCS8");

        override fun toString(): String {
            return value
        }
    }

    enum class PemAlgorithm(val value: String) {
        UNKNOWN("UNKNOWN"),
        RSA("RSA"),
        EC("EC");

        override fun toString(): String {
            return value
        }
    }

    data class PemInfo(
        val type: PemType,
        val format: PemFormat,
        val algorithm: PemAlgorithm,
    )
}
