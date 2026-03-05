package com.fatec.lddm_merge_skills.repository

import com.fatec.lddm_merge_skills.model.Question

interface QuestionRepository {
    suspend fun getByLessonId(lessonId: Int): List<Question>
    suspend fun getById(id: Int): Question?
}