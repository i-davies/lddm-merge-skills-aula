package com.fatec.lddm_merge_skills.routes

import com.fatec.lddm_merge_skills.repository.CourseRepository
import com.fatec.lddm_merge_skills.repository.LessonRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.courseRoutes(
    courseRepository: CourseRepository,
    lessonRepository: LessonRepository
) {
    // GET /courses → Lista todos os cursos
    get("/courses") {
        val courses = courseRepository.getAll()
        call.respond(courses)
    }

    // GET /courses/{id}/lessons → Lições de um curso
    get("/courses/{id}/lessons") {
        val id = call.parameters["id"]?.toIntOrNull()
        if (id == null) {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID inválido"))
            return@get
        }
        val lessons = lessonRepository.getByCourseId(id)
        call.respond(lessons)
    }
}