package com.fatec.lddm_merge_skills

import com.fatec.lddm_merge_skills.db.DatabaseFactory
import com.fatec.lddm_merge_skills.db.ExposedCourseRepository
import com.fatec.lddm_merge_skills.db.ExposedLessonRepository
import com.fatec.lddm_merge_skills.db.ExposedQuestionRepository
import com.fatec.lddm_merge_skills.routes.courseRoutes
import com.fatec.lddm_merge_skills.routes.lessonRoutes
import com.fatec.lddm_merge_skills.routes.progressRoutes
import com.fatec.lddm_merge_skills.routes.questionRoutes
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.github.cdimascio.dotenv.dotenv

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val dotenv = dotenv()
    val useSupabase = dotenv["USE_SUPABASE"]?.toBoolean() ?: false

    install(ContentNegotiation) { json() }
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError,
                mapOf("error" to (cause.message ?: "Erro interno")))
        }
    }

    if (!useSupabase) {
        DatabaseFactory.init() // Engine Local
    }

    //  Instanciando os repositórios (Exposed = banco real)
    val courseRepository: com.fatec.lddm_merge_skills.repository.CourseRepository
    val lessonRepository: com.fatec.lddm_merge_skills.repository.LessonRepository
    val questionRepository: com.fatec.lddm_merge_skills.repository.QuestionRepository

    if (useSupabase) {
        courseRepository = com.fatec.lddm_merge_skills.db.SupabaseCourseRepository()
        lessonRepository = com.fatec.lddm_merge_skills.db.SupabaseLessonRepository()
        questionRepository = com.fatec.lddm_merge_skills.db.SupabaseQuestionRepository()
        println("\n Usando banco na nuvem: Supabase (PostgREST)")
    } else {
        courseRepository = ExposedCourseRepository()
        lessonRepository = ExposedLessonRepository()
        questionRepository = ExposedQuestionRepository()
        println("\n Usando banco local: Docker (PostgreSQL + Exposed)")
    }

    routing {
        get("/") { call.respondText("Ktor: ${Greeting().greet()}") }
        get("/health") { call.respondText("OK") }

        swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")

        // Passando os repositórios para as rotas
        courseRoutes(courseRepository, lessonRepository)
        lessonRoutes(questionRepository)
        questionRoutes(questionRepository)
        progressRoutes(questionRepository)
    }
}