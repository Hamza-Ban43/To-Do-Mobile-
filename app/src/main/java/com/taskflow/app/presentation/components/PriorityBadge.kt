package com.taskflow.app.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PriorityBadge(priority: String) {
    val priorityColor = when (priority) {
        "High" -> Color(0xFFE53935)
        "Medium" -> Color(0xFFFB8C00)
        else -> Color(0xFF43A047)
    }

    Surface(
        shape = RoundedCornerShape(6.dp),
        color = priorityColor.copy(alpha = 0.15f)
    ) {
        Text(
            text = priority,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = priorityColor,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}
