dependencies {
    api(libs.jsonwebtoken.jjwt.api) // Jwt Json Token 验证库
    api(libs.jsonwebtoken.jjwt.impl) // jjwt 依赖库
    api(libs.jsonwebtoken.jjwt.jackson) // jjwt 依赖库
    testImplementation(kotlin("test")) // Kotlin 测试依赖
}

tasks.test {
    useJUnitPlatform()
}
