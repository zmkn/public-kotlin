plugins {
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    api(project(":service-kotlin"))
    api(libs.kotlin.reflect) // Kotlin 反射库
    api(libs.kotlinx.datetime) // Kotlin 日期类型序列化支持库
    api(libs.kotlinx.serialization.json) // Kotlin json 序列化库
    api(libs.kotlinx.coroutines.core) // Kotlin 协程库
    api(libs.kotlinx.coroutines.reactive) // Kotlin 协程 reactive 支持库
    api(libs.kotlinx.datetime.bson.codec) // Kotlinx datetime codec for bson
    api(libs.kmongo.coroutine) // MongoDB 支持库
    api(libs.kmongo.coroutine.serialization) // KMongo KotlinX serialization 支持库
    api(libs.kmongo.id) // KMongo Id 支持库
    api(libs.kmongo.id.jackson) // KMongo Id Jackson serialization 支持库
    api(libs.kmongo.id.serialization) // KMongo Id KotlinX serialization 支持库
    api(libs.victools.jsonschema.generator) // dashscope sdk 依赖库
    api(libs.dashscope.sdk.java) // 阿里百炼平台支持库
}
