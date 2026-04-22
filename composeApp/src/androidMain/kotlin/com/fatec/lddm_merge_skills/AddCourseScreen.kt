package com.fatec.lddm_merge_skills

import android.R
import android.view.Surface
import android.widget.Button
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

@Composable
fun AddCourseScreen( onBack: () -> Unit, onCourseCreated: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var saving by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope ()

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White){
        Column(modifier = Modifier.fillMaxSize().statusBarsPadding().padding(16.dp)) {
            // Header
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)

            ) {
                OutlinedButton(onClick = onBack) { Text("\u276E", color = Color.Black, fontSize = 16.sp) }
                Column {
                    Text("Novo Curso", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                    Text("Preencha os detalhes abaixo.", fontSize = 14.sp, color = Muted)
                }
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Border)

            // Formulário

            Text("Título", fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(4.dp))
            OutlinedTextField(value = title, onValueChange = {title = it}, modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(16.dp))

            Text("Descrição", fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(4.dp))
            OutlinedTextField(value = description, onValueChange = {description = it}, modifier = Modifier.fillMaxWidth())

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (title.isBlank()) {
                        message = "O título é obrigatório."
                        isError = true
                        return@Button
                    }
                    scope.launch {
                        saving = true
                        try {
                            ApiClient.createCourse(
                                title.trim(),
                                description.trim().ifBlank { null }
                            )
                            message = "Curso criado!"
                            isError = false
                            kotlinx.coroutines.delay(600)
                            onCourseCreated()

                        } catch (e: Exception) {
                            message = "Erro: ${e.message}"
                            isError = true
                        }
                        saving = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !saving,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White)
            ) {
                if (saving) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = Color.White);
                    Spacer(Modifier.width(8.dp))
                }
                Text(if (saving) "Salvando ..." else "Salvar Curso", color = Color.White)
            }

            if (message != null) {
                Spacer(Modifier.height(12.dp))
                Text(message!!, color = if (isError) Color.Red else Color(0xFF16A34A))
            }
        }
    }
}