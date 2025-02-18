plugins {
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    api(libs.kotlin.reflect) // Kotlin 反射库
    api(libs.kotlinx.datetime) // Kotlin 日期类型序列化支持库
    api(libs.kotlinx.serialization.json) // Kotlin json 序列化库
    api(libs.kotlinx.coroutines.core) // Kotlin 协程库
    api(libs.protobuf.java.util) // Protobuf java 支持库
    api(libs.kotlinx.datetime.jackson.module) // Kotlinx datetime module for jackson
    api(libs.mongodb.driver.reactivestreams) // MongoDB 反应流异步驱动器库，kmongo 需要
    api(libs.mongodb.driver.sync) // MongoDB 同步驱动器库，morphia 需要
    api(libs.mongodb.driver.legacy) // MongoDB 向后兼容同步驱动器库，morphia 需要
    // MongoDB 支持库
    api(libs.morphia.kotlin) {
        // 排除 mongodb 相关依赖库，由上面手动加载，防止与 kmongo 冲突
        exclude(group = "org.mongodb")
    }
    api(libs.ehcache) // 本地缓存插件
    api(libs.javax.jaxb.api) // ehcache 依赖此库
    api(libs.glassfish.jaxb.runtime) // ehcache 依赖此库
    api(libs.bouncycastle.bcprov.jdk18on) // Bouncy Castle 加解密数字签名库
    api(libs.bouncycastle.bcpkix.jdk18on) // Bouncy Castle 依赖库
    api(libs.snakeyaml.engine) // snakeyaml-engine YML工具库
    api(libs.jackson.databind) // JSON 序列化库
    api(libs.jackson.module.kotlin) // jackson 支持 kotlin 模块库
}
