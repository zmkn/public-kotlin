dependencies {
    api(libs.protobuf.java.util)
    api(libs.zmkn.serialization.jackson.kotlin) // Jackson 工具库
    api(libs.mongodb.bson) // MongoDB Bson 支持库
    api(platform(libs.tools.jackson.bom)) // Jackson Bom 物料库
    api(libs.tools.jackson.databind) // JSON 序列化库
}
