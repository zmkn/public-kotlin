plugins {
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    api(libs.kotlin.reflect) // Kotlin 反射库
    api(libs.kotlinx.serialization.json) // Kotlin json 序列化库
    api(libs.jsonwebtoken.jjwt.api) // Jwt Json Token 验证库
    api(libs.jsonwebtoken.jjwt.impl) // jjwt 依赖库
    api(libs.jsonwebtoken.jjwt.jackson) // jjwt 依赖库
}
