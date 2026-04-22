package com.fatec.lddm_merge_skills.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fatec.lddm_merge_skills.App
import com.fatec.lddm_merge_skills.model.Course
import com.fatec.lddm_merge_skills.ui.theme.AppColors

@Composable
fun CourseCard(
    course: Course,
    modifer:  Modifier = Modifier
) {

    OutlinedCard (
        modifier = modifer.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, AppColors.Border),
        colors = CardDefaults.outlinedCardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(course.title, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            val desc = course.description
            if(!desc.isNullOrBlank()) {
                Text(desc, fontSize = 13.sp, color = AppColors.Muted)
            }
        }

    }

}