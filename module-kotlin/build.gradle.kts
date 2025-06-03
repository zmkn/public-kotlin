plugins {
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    api(project(":util-kotlin"))
    api(project(":service-kotlin"))
    api(project(":extension-kotlin"))
    api(project(":enumeration-kotlin"))
    api(libs.kotlin.reflect) // Kotlin 反射库
    api(libs.kotlinx.datetime) // Kotlin 日期类型序列化支持库
    api(libs.kotlinx.serialization.json) // Kotlin json 序列化库
    api(libs.kotlinx.coroutines.core) // Kotlin 协程库
    api(libs.kotlinx.coroutines.reactive) // Kotlin 协程 reactive 支持库
    api(libs.kotlinx.datetime.bson.codec) // Kotlinx datetime codec for bson
    api(libs.bson.kotlin.serializers.module) // Bson serializers module for kotlin
    api(libs.bson.jackson.module) // Bson module for jackson
    api(libs.zmkn.srcc.nacos.kotlin) // Nacos 工具库
    api(libs.zmkn.serialization.jackson.kotlin) // Jackson 工具库
    api(libs.kmongo.coroutine) // MongoDB 支持库
    api(libs.kmongo.coroutine.serialization) // KMongo KotlinX serialization 支持库
    api(libs.kmongo.id) // KMongo Id 支持库
    api(libs.kmongo.id.jackson) // KMongo Id Jackson serialization 支持库
    api(libs.kmongo.id.serialization) // KMongo Id KotlinX serialization 支持库
    api(platform(libs.squareup.okhttp3.okhttp.bom)) // Okhttp Bom 物料库
    api(libs.squareup.okhttp3.okhttp) // Okhttp 库
    api(libs.victools.jsonschema.generator) // dashscope sdk 依赖库
    // 阿里百炼平台支持库
    api(libs.dashscope.sdk.java) {
        // 排除 slf4j-simple 依赖库
        exclude(group = "org.slf4j", module = "slf4j-simple")
    }
    api(libs.apache.commons.pool2) // 阿里百炼 Dashscope CosyVoice 服务依赖库
    api(libs.aliyun.dysmsapi) // 阿里云短信平台支持库
    api(libs.aliyun.sdk.oss) // 阿里云 OSS 平台支持库
}
