package com.taskflow.app.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taskflow.app.data.local.TaskEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TaskCard(
    task: TaskEntity,
    onTaskClick: (TaskEntity) -> Unit,
    onToggleCompletion: (TaskEntity, Boolean) -> Unit,
    onShareClick: (TaskEntity) -> Unit,
    onDeleteClick: (TaskEntity) -> Unit
) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = { isChecked ->
                onToggleCompletion(task, isChecked)
            }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .clickable { onTaskClick(task) }
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = task.title,
                fontSize = 18.sp,
                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
            )

            val detailsText = buildString {
                append("${task.priority} • ${task.category}")
                task.dueDate?.let {
                    append(" • ${dateFormat.format(Date(it))}")
                }
            }

            Text(
                text = detailsText,
                fontSize = 14.sp
            )
        }

        TextButton(
            onClick = { onShareClick(task) }
        ) {
            Text("Share")
        }

        Button(
            onClick = { onDeleteClick(task) }
        ) {
            Text("X")
        }
    }
}
