package com.fatec.lddm_merge_skills.repository

import com.fatec.lddm_merge_skills.model.Course

interface CourseRepository {
    suspend fun getAll(): List<Course>

    suspend fun getById(id: Int): Course?

    suspend fun create(course: Course): Course

    suspend fun update(id: Int, course: Course): Course

    suspend fun delete(id: Int)
}