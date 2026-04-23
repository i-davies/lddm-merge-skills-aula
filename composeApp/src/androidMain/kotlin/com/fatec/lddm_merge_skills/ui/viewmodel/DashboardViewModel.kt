package com.fatec.lddm_merge_skills.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fatec.lddm_merge_skills.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DashboardUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val coursesCount: Int = 0,
    val lessonsCount: Int = 0,
    val questionCount: Int = 0
)

class DashboardViewModel : ViewModel() {
    // Flux interno apenas o ViewModel que modifica
    private val _uiState = MutableStateFlow(DashboardUiState())

    // Fluxo publico, ele só observa o que é alterado
    val uiState : StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState(isLoading = true)

            try {
                val courses = ApiClient.getCourses()
                var totalLesson = 0
                var totalQuestions = 0

                for (course in courses) {
                    val lessons = ApiClient.getLessons(course.id)
                    totalLesson += lessons.size
                    for (lesson in lessons) {
                        totalQuestions += ApiClient.getQuestions(lesson.id).size
                    }
                }

                // Estado completo com todos os dados
                _uiState.value = DashboardUiState(
                    isLoading = false,
                    coursesCount = courses.size,
                    lessonsCount = totalLesson,
                    questionCount = totalQuestions
                )

            } catch (e: Exception) {
                _uiState.value = DashboardUiState(
                    isLoading = false,
                    error = e.message ?: "Erro desconhecido"
                )
            }

        }
    }


}