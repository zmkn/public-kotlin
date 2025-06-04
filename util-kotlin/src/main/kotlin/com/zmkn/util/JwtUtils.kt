package com.zmkn.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwt
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.SignatureAlgorithm
import java.security.PrivateKey
import java.security.PublicKey
import java.util.*
import javax.crypto.SecretKey

object JwtUtils {
    fun create(
        jti: String,
        issuer: String,
        subject: String,
        issuedAt: Date,
        notBefore: Date = issuedAt,
        expiration: Date,
        headers: Map<String, Any> = mapOf(),
        audiences: Collection<String> = setOf(),
        claims: Map<String, Any> = mapOf(),
        privateKey: PrivateKey,
        alg: SignatureAlgorithm,
    ): String {
        return Jwts.builder()
            .id(jti) // 设置 JWT ID
            .issuer(issuer) // 设置 JWT 的签发者
            .subject(subject) // 设置 JWT 的主题
            .issuedAt(issuedAt) // 设置 JWT 的创建时间
            .notBefore(notBefore) // 设置 JWT 的生效时间
            .expiration(expiration) // 设置 JWT 的过期时间
            .header().add(headers).and() // 设置 JWT 的头
            .audience().add(audiences).and() // 设置 JWT 的受众
            .claims(claims) // 设置 JWT 的自定义的声明
            .signWith(privateKey, alg)
            .compact()
    }

    fun parseToken(token: CharSequence, secretKey: SecretKey): Jwt<*, *> {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parse(token)
    }

    fun parseToken(token: CharSequence, publicKey: PublicKey): Jwt<*, *> {
        return Jwts.parser()
            .verifyWith(publicKey)
            .build()
            .parse(token)
    }

    fun parseSignedToken(token: CharSequence, secretKey: SecretKey): Jws<Claims> {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
    }

    fun parseSignedToken(token: CharSequence, publicKey: PublicKey): Jws<Claims> {
        return Jwts.parser()
            .verifyWith(publicKey)
            .build()
            .parseSignedClaims(token)
    }

    fun verifyToken(token: CharSequence, secretKey: SecretKey): Boolean {
        return try {
            parseSignedToken(token, secretKey)
            true
        } catch (exc: Exception) {
            false
        }
    }

    fun verifyToken(token: CharSequence, publicKey: PublicKey): Boolean {
        return try {
            parseSignedToken(token, publicKey)
            true
        } catch (exc: Exception) {
            false
        }
    }
}
