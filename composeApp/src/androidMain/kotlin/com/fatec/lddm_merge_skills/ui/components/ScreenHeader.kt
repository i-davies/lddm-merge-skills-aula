package com.fatec.lddm_merge_skills.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fatec.lddm_merge_skills.ui.theme.AppColors

@Composable
fun ScreenHeader(
    title : String,
    subtitle: String,
    onBack: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onBack,
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, AppColors.Border),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Text("\u276E", color = Color.Black, fontSize = 16.sp)
                }

                Column {
                    Text(title, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                    Text(subtitle, fontSize = 14.sp, color = AppColors.Muted)
                }

            }

            // Slot para ações opcionais à direita
            Row{ actions() }

        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 12.dp),
            color = AppColors.Border
        )

    }

}