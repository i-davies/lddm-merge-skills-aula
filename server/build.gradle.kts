plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlinSerialization)
    application
}

group = "com.fatec.lddm_merge_skills"
version = "1.0.0"
application {
    mainClass.set("com.fatec.lddm_merge_skills.ApplicationKt")
    
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)

    // Serialização JSON para respostas da API
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    // ORM Jetbrains Exposed
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.json)

    // Controle de esquemas - Flyway
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)

    // Interação oficial JDBC
    implementation(libs.postgresql)

    // Swagger UI
    implementation(libs.ktor.server.swagger)
    implementation(libs.ktor.server.status.pages)

    implementation(libs.supabase.postgrest.kt)
    implementation(libs.ktor.client.cio.jvm)
    implementation(libs.dotenv)
}