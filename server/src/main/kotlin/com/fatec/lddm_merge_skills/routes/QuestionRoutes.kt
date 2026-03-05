package com.fatec.lddm_merge_skills.routes

import com.fatec.lddm_merge_skills.repository.QuestionRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.questionRoutes(questionRepository: QuestionRepository) {
    get("/questions/{id}") {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID inválido"))
            return@get
        }
        val question = questionRepository.getById(id)
        if (question == null) {
            call.respond(HttpStatusCode.NotFound, mapOf("error" to "Questão não encontrada"))
            return@get
        }
        call.respond(question)
    }
}