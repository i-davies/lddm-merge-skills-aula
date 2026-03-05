package com.fatec.lddm_merge_skills.routes

import com.fatec.lddm_merge_skills.repository.QuestionRepository
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

// DTOs (Data Transfer Objects) — objetos usados na API
@Serializable
data class SubmitAnswerRequest(val userId: Int, val questionId: Int, val selectedOption: Int)

@Serializable
data class SubmitAnswerResponse(val isCorrect: Boolean, val correctAnswer: Int, val message: String)

@Serializable
data class ResetProgressRequest(val userId: Int, val lessonId: Int)

fun Route.progressRoutes(questionRepository: QuestionRepository) {
    // POST /progress/submit → Verifica resposta
    post("/progress/submit") {
        val request = call.receive<SubmitAnswerRequest>()
        val question = questionRepository.getById(request.questionId)
        if (question == null) {
            call.respond(HttpStatusCode.NotFound, mapOf("error" to "Questão não encontrada"))
            return@post
        }
        val isCorrect = request.selectedOption == question.correctAnswer
        call.respond(SubmitAnswerResponse(
            isCorrect = isCorrect,
            correctAnswer = question.correctAnswer ?: 0,
            message = if (isCorrect) "Resposta correta! " else "Tente novamente!"
        ))
    }

    // POST /progress/reset → Reseta progresso
    post("/progress/reset") {
        val request = call.receive<ResetProgressRequest>()
        call.respond(mapOf("message" to "Progresso resetado para lição ${request.lessonId}"))
    }

    // GET /progress/history/{userId} → Histórico (mock)
    get("/progress/history/{userId}") {
        val userId = call.parameters["userId"]?.toIntOrNull()
        if (userId == null) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID inválido"))
            return@get
        }
        call.respond(mapOf("completedLessons" to emptyList<Int>()))
    }
}