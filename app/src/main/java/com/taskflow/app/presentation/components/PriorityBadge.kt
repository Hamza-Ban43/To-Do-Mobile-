package com.taskflow.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taskflow.app.ui.theme.priorityBadgeColors

@Composable
fun PriorityBadge(
    priority: String,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val badgeColors = priorityBadgeColors(priority, isDark)

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = badgeColors.background
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(badgeColors.text, CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = priority,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = badgeColors.text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp
            )
        }
    }
}
