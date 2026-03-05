package com.fatec.lddm_merge_skills.routes

import com.fatec.lddm_merge_skills.repository.QuestionRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.lessonRoutes(questionRepository: QuestionRepository) {
    get("/lessons/{id}/questions") {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID inválido"))
            return@get
        }
        val questions = questionRepository.getByLessonId(id)
        call.respond(questions)
    }
}