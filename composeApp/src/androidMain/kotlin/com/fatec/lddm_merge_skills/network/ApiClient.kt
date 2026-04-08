package com.fatec.lddm_merge_skills.network

import com.fatec.lddm_merge_skills.BASE_URL
import com.fatec.lddm_merge_skills.model.Course
import com.fatec.lddm_merge_skills.model.Lesson
import com.fatec.lddm_merge_skills.model.Question
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object ApiClient {

    private val httpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                prettyPrint = false
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30000
            connectTimeoutMillis = 10000
            socketTimeoutMillis = 10000
        }
    }

    /** GET /courses → Lista todos os cursos */
    suspend fun getCourses(): List<Course> {
        return httpClient.get("$BASE_URL/courses").body()
    }

    suspend fun getLessons(courseId: Int): List<Lesson> {
        return httpClient.get("$BASE_URL/courses/$courseId/lessons").body()
    }

    suspend fun getQuestions(lessonId: Int): List<Question> {
        return httpClient.get("$BASE_URL/lessons/$lessonId/questions").body()
    }

    /** POST /courses → Cria um novo curso */
    suspend fun createCourse(title: String, description: String?): Course {
        return httpClient.post("$BASE_URL/courses") {
            contentType(ContentType.Application.Json)
            setBody(Course(title = title, description = description))
        }.body()
    }
}