dependencies {
    api(libs.protobuf.java.util)
    api(libs.mongodb.bson) // MongoDB Bson 支持库
    api(libs.jackson.databind) // JSON 序列化库
    api(libs.jackson.module.kotlin) // jackson 支持 kotlin 模块库
}
