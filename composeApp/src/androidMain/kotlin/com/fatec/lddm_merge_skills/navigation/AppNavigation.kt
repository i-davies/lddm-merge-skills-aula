package com.fatec.lddm_merge_skills.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.fatec.lddm_merge_skills.AddCourseScreen
import com.fatec.lddm_merge_skills.CourseListScreen
import com.fatec.lddm_merge_skills.DashboardScreen
import com.fatec.lddm_merge_skills.Screen

@Composable
fun AppNavigation() {
    MaterialTheme {

        var currentScreen by remember { mutableStateOf(com.fatec.lddm_merge_skills.Screen.DASHBOARD) }

        when (currentScreen) {
            com.fatec.lddm_merge_skills.Screen.DASHBOARD -> DashboardScreen(
                onNavigateToCourses = { currentScreen = com.fatec.lddm_merge_skills.Screen.COURSES }
            )
            com.fatec.lddm_merge_skills.Screen.COURSES -> CourseListScreen(
                onBack = { currentScreen = com.fatec.lddm_merge_skills.Screen.DASHBOARD},
                onAddCourse = {currentScreen = com.fatec.lddm_merge_skills.Screen.ADD_COURSE}
            )
            com.fatec.lddm_merge_skills.Screen.ADD_COURSE -> AddCourseScreen(
                onBack = { currentScreen = com.fatec.lddm_merge_skills.Screen.COURSES},
                onCourseCreated = { currentScreen = Screen.COURSES}
            )
        }
    }
}