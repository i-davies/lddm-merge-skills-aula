package com.fatec.lddm_merge_skills.db

import com.fatec.lddm_merge_skills.model.Question
import com.fatec.lddm_merge_skills.repository.QuestionRepository
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class ExposedQuestionRepository : QuestionRepository {

    // Converte ResultRow para Question
    // ATENÇÃO: o campo options precisa de conversão JSON → List<String>
    private fun ResultRow.toQuestion(): Question {
        val optionsJson = this[Questions.options]
        val optionsList: List<String> = try {
            Json.decodeFromString(optionsJson)
        } catch (e: Exception) {
            emptyList()
        }

        return Question(
            id = this[Questions.id].value,
            lessonId = this[Questions.lessonId].value,
            question = this[Questions.question],
            code = this[Questions.code],
            options = optionsList,
            correctAnswer = this[Questions.correctAnswer],
            order = this[Questions.order]
        )
    }

    override suspend fun getByLessonId(lessonId: Int): List<Question> = newSuspendedTransaction {
        Questions.selectAll()
            .where { Questions.lessonId eq lessonId }
            .orderBy(Questions.order)
            .map { it.toQuestion() }
    }

    override suspend fun getById(id: Int): Question? = newSuspendedTransaction {
        Questions.selectAll()
            .where { Questions.id eq id }
            .map { it.toQuestion() }
            .singleOrNull()
    }
}