package com.fatec.lddm_merge_skills.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.fatec.lddm_merge_skills.ui.screens.AddCourseScreen
import com.fatec.lddm_merge_skills.ui.screens.CourseListScreen
import com.fatec.lddm_merge_skills.ui.screens.DashboardScreen

@Composable
fun AppNavigation() {
    MaterialTheme {

        var currentScreen by remember { mutableStateOf(Screen.DASHBOARD) }

        when (currentScreen) {
            Screen.DASHBOARD -> DashboardScreen(
                onNavigateToCourses = { currentScreen = Screen.COURSES }
            )
            Screen.COURSES -> CourseListScreen(
                onBack = { currentScreen = Screen.DASHBOARD },
                onAddCourse = { currentScreen = Screen.ADD_COURSE }
            )
            Screen.ADD_COURSE -> AddCourseScreen(
                onBack = { currentScreen = Screen.COURSES },
                onCourseCreated = { currentScreen = Screen.COURSES }
            )
        }
    }
}