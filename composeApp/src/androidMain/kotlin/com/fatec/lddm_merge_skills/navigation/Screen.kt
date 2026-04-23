package com.fatec.lddm_merge_skills.navigation

import kotlinx.serialization.Serializable


// navController.navigate(Screen.Dashboard)

sealed class Screen {
    @Serializable data object Dashboard: Screen()

    @Serializable data object Courses : Screen()

    @Serializable data object AddCourse: Screen()
}