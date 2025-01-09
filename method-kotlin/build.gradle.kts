dependencies {
    testImplementation(kotlin("test")) // Kotlin 测试依赖
}

tasks.test {
    useJUnitPlatform()
}
