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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
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
import com.fatec.lddm_merge_skills.model.Course
import com.fatec.lddm_merge_skills.network.ApiClient
import com.fatec.lddm_merge_skills.ui.components.CourseCard
import com.fatec.lddm_merge_skills.ui.components.ScreenHeader
import com.fatec.lddm_merge_skills.ui.theme.AppColors
import kotlinx.coroutines.launch

@Composable
fun CourseListScreen(onBack: () -> Unit, onAddCourse: () -> Unit) {
    var courses by remember { mutableStateOf<List<Course>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

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
        Column(modifier = Modifier.fillMaxSize().statusBarsPadding().padding(16.dp, vertical = 20.dp)) {

            // Componente reutilizável
            ScreenHeader(
                title = "Cursos",
                subtitle = "Gerencie seus cursos.",
                onBack = onBack,
                actions = {
                    Button(
                        onClick = onAddCourse,
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White)
                    ) {
                        Text("+ Adicionar", fontSize = 13.sp)

                    }
                }
            )

            // 3. Renderização sob Demanda!
            when {
                loading -> { Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator(color = AppColors.Muted) } }
                error != null -> {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Text("Erro: ${error.orEmpty()}", color = Color.Red)
                    }
                }
                else -> {
                    // LazyColumn faria o carregamento sob-demanda em memória criando apenas os Cards
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(courses) { course ->
                            CourseCard(course)
                        }
                    }
                }
            }
        }
    }
}