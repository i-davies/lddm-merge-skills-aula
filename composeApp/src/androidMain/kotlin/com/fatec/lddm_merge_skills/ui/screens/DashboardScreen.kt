package com.fatec.lddm_merge_skills.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fatec.lddm_merge_skills.DashboardCard
import com.fatec.lddm_merge_skills.network.ApiClient
import com.fatec.lddm_merge_skills.ui.theme.AppColors
import kotlinx.coroutines.launch

@Composable
fun DashboardScreen(onNavigateToCourses: () -> Unit) {

    // Estados
    var coursesCount by remember { mutableStateOf(0) }
    var lessonsCount by remember { mutableStateOf(0) }
    var questionsCount by remember { mutableStateOf(0) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    // Escopo que permite lançar tarefas assíncronas
    val scope = rememberCoroutineScope ()

    fun refresh() {
        scope.launch {
            loading = true
            error = null

            try {
                val courses = ApiClient.getCourses()
                coursesCount = courses.size

                var totalLessons = 0
                var totalQuestions = 0

                for (course in courses) {
                    val lessons = ApiClient.getLessons(course.id)
                    totalLessons += lessons.size
                    for (lesson in lessons) {
                        val questions = ApiClient.getQuestions(lesson.id)
                        totalQuestions += questions.size
                    }
                }

                lessonsCount = totalLessons
                questionsCount = totalQuestions

            } catch (e: Exception) {
                error = e.message
            }

            loading = false
        }
    }

    LaunchedEffect(Unit) { refresh() }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column (
            modifier = Modifier.fillMaxSize().statusBarsPadding().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(Modifier.fillMaxWidth(),
                Arrangement.SpaceBetween,
                Alignment.CenterVertically)
            {
                Column {
                    Text("Painel Principal", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text("Visão Geral do Sistema", fontSize = 14.sp, color = AppColors.Muted)
                }
                OutlinedButton(
                    onClick = { refresh() },
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, AppColors.Border)
                ) {
                    Text("Atualizar", color = Color.Black)
                }
            }

            HorizontalDivider(color = AppColors.Border)

            if(loading) {
                Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center){
                    CircularProgressIndicator(color = AppColors.Muted, strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
                }
            }
            else if (error != null) {
                Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center){
                    Text("Erro: $error", color = Color.Red, fontSize = 14.sp)
                }
            }
            else {
                // Cards
                Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(12.dp)) {
                    DashboardCard("Cursos", coursesCount.toString(), Modifier.weight(1f))
                    DashboardCard("Lições", lessonsCount.toString(), Modifier.weight(1f))
                    DashboardCard("Questões", questionsCount.toString(), Modifier.weight(1f))
                }

                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = onNavigateToCourses,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("Gerenciar Cursos testando", color = Color.White)
                }
            }
        }
    }
}
