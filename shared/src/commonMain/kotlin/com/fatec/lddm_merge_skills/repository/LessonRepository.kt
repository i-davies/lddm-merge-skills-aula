package com.fatec.lddm_merge_skills.repository

import com.fatec.lddm_merge_skills.model.Lesson

interface LessonRepository {
    suspend fun getByCourseId(courseId: Int): List<Lesson>
    suspend fun getById(id: Int): Lesson?
}