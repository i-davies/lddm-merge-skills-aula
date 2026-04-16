package com.fatec.lddm_merge_skills

import android.R
import android.view.Surface
import android.widget.Space
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fatec.lddm_merge_skills.model.Course
import com.fatec.lddm_merge_skills.network.ApiClient
import kotlinx.coroutines.launch

// ─── Cores shadcn/ui (tema claro) ───
private val Border = Color(0xFFE5E7EB)
private val Muted = Color(0xFF6B7280)

enum class Screen  { DASHBOARD, COURSES, ADD_COURSE }

@Composable
fun App() {
    MaterialTheme {
        //DashboardScreen()

        var currentScreen by remember { mutableStateOf(Screen.DASHBOARD) }

        when (currentScreen) {
            Screen.DASHBOARD -> DashboardScreen(
                onNavigateToCourses = { currentScreen = Screen.COURSES }
            )
            Screen.COURSES -> CourseListScreen(
                onBack = { currentScreen = Screen.DASHBOARD},
                onAddCourse = {currentScreen = Screen.ADD_COURSE}
            )
            Screen.ADD_COURSE -> AddCourseScreen(
//                onBack = { currentScreen = Screen.COURSES},
//                onCourseCreated = { currentScreen = Screen.COURSES}
            )
        }
    }
}

@Composable
fun AddCourseScreen () {

}

@Composable
fun DashboardScreen(onNavigateToCourses: () -> Unit) {

    // Estados
    var coursesCount by remember { mutableStateOf(0) }
    var lessonsCount by remember { mutableStateOf(0) }
    var questionsCount by remember { mutableStateOf(0) }
    var loading by remember { mutableStateOf(true) }

    // Escopo que permite lançar tarefas assíncronas
    val scope = rememberCoroutineScope ()

    fun refresh() {
        scope.launch {
            loading = true

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
                e.printStackTrace()
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
                    Text("Visão Geral do Sistema", fontSize = 14.sp, color = Muted)
                }
                OutlinedButton(onClick = { }) {
                    Text("Atualizar", color = Color.Black)
                }
            }

            HorizontalDivider(color = Border)

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
                Text("Gerenciar Cursos", color = Color.White)
            }
        }
    }
}

@Composable
fun DashboardCard(title: String, value: String, modifier: Modifier = Modifier) {
    OutlinedCard(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Border),
        colors = CardDefaults.outlinedCardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, fontSize = 13.sp, color = Muted, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(4.dp))
            Text(value, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CourseListScreen(onBack: () -> Unit, onAddCourse: () -> Unit) {
    // 1. Variaveis Observáveis e Contexto Suspenso (A Memória da Tela)

    var courses by remember { mutableStateOf<List<Course>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // 2. Busca Dinânica
    fun refresh() {
        scope.launch {
            loading = true
            error = null
            try { courses = ApiClient.getCourses() } catch (e: Exception) { error = e.message }
            loading = false
        }
    }
    LaunchedEffect(Unit) { refresh() }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(modifier = Modifier.fillMaxSize().statusBarsPadding().padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Callback Passivo Elevado (Event Hoisting) - Resposta volta ao Root Navigation
                    OutlinedButton(onClick = onBack) { Text("\u276E", color = Color.Black, fontSize = 16.sp) }
                    Column {
                        Text("Cursos", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                        Text("Gerencie seus cursos.", fontSize = 14.sp, color = Muted)
                    }
                }
                Button(onClick = onAddCourse, colors = ButtonDefaults.buttonColors(containerColor = Color.Black)) { Text("+ Adicionar", color = Color.White) }
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Border)

            // 3. Renderização sob Demanda!
            when {
                loading -> { Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator(color = Muted) } }
                error != null -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Falha na Conexão", fontWeight = FontWeight.Medium)
                            Text(error.orEmpty(), fontSize = 13.sp, color = Muted)
                            Spacer(Modifier.height(12.dp))
                            OutlinedButton(onClick = { refresh() }) { Text("Tentar Novamente", color = Color.Black) }
                        }
                    }
                }
                else -> {
                    // LazyColumn faria o carregamento sob-demanda em memória criando apenas os Cards
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(courses) { course ->
                            OutlinedCard(Modifier.fillMaxWidth(), border = BorderStroke(1.dp, Border), colors = CardDefaults.outlinedCardColors(containerColor = Color.White)) {
                                Column(Modifier.padding(14.dp)) {
                                    Text(course.title, fontWeight = FontWeight.Medium)
                                    course.description?.let { Text(it, fontSize = 13.sp, color = Muted) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}