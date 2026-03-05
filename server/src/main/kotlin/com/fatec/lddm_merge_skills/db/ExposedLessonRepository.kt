package com.fatec.lddm_merge_skills.db

import com.fatec.lddm_merge_skills.model.Lesson
import com.fatec.lddm_merge_skills.repository.LessonRepository
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class ExposedLessonRepository : LessonRepository {

    private fun ResultRow.toLesson() = Lesson(
        id = this[Lessons.id].value,
        courseId = this[Lessons.courseId].value,
        title = this[Lessons.title],
        description = this[Lessons.description],
        order = this[Lessons.order],
    )

    override suspend fun getByCourseId(courseId: Int): List<Lesson> = newSuspendedTransaction {
        Lessons.selectAll()
            .where { Lessons.courseId eq courseId }
            .orderBy(Lessons.order)
            .map { it.toLesson() }
    }

    override suspend fun getById(id: Int): Lesson? = newSuspendedTransaction {
        Lessons.selectAll()
            .where { Lessons.id eq id }
            .map { it.toLesson() }
            .singleOrNull()
    }
}