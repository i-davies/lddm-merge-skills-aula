package com.fatec.lddm_merge_skills.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fatec.lddm_merge_skills.network.ApiClient
import com.fatec.lddm_merge_skills.ui.components.ScreenHeader
import com.fatec.lddm_merge_skills.ui.theme.AppColors
import kotlinx.coroutines.launch

@Composable
fun AddCourseScreen(onBack: () -> Unit, onCourseCreated: () -> Unit) {
    var title       by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var saving      by remember { mutableStateOf(false) }
    var message     by remember { mutableStateOf<String?>(null) }
    var isError     by remember { mutableStateOf(false) }
    val scope       = rememberCoroutineScope()

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(Modifier.fillMaxSize().statusBarsPadding().padding(horizontal = 16.dp, vertical = 20.dp)) {

            // Mesmo componente, sem botão à direita!
            ScreenHeader(
                title = "Novo Curso",
                subtitle = "Preencha os detalhes abaixo.",
                onBack = onBack
            )

            // Formulário
            Text("Título *", fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(4.dp))
            OutlinedTextField(
                value = title, onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp), singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = AppColors.Border,
                    focusedBorderColor = Color.Black
                )
            )

            Spacer(Modifier.height(16.dp))

            Text("Descrição", fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(4.dp))
            OutlinedTextField(
                value = description, onValueChange = { description = it },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(6.dp), maxLines = 5,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = AppColors.Border,
                    focusedBorderColor = Color.Black
                )
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (title.isBlank()) { message = "O título é obrigatório."; isError = true; return@Button }
                    scope.launch {
                        saving = true; message = null
                        try {
                            ApiClient.createCourse(title.trim(), description.trim().ifBlank { null })
                            message = "Curso criado!"; isError = false
                            kotlinx.coroutines.delay(600)
                            onCourseCreated()
                        } catch (e: Exception) { message = "Erro: ${e.message}"; isError = true }
                        saving = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp),
                enabled = !saving,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White)
            ) {
                if (saving) {
                    CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp, color = Color.White)
                    Spacer(Modifier.width(8.dp))
                }
                Text(if (saving) "Salvando..." else "Salvar Curso", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }

            if (message != null) {
                Spacer(Modifier.height(12.dp))
                Text(message!!, fontSize = 13.sp, fontWeight = FontWeight.Medium,
                    color = if (isError) AppColors.ErrorRed else AppColors.SuccessGreen)
            }
        }
    }
}