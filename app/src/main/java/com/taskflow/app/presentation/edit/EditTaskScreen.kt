package com.taskflow.app.presentation.edit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taskflow.app.data.local.TaskEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun EditTaskScreen(
    task: TaskEntity,
    onSave: (TaskEntity) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val priorities = listOf("High", "Medium", "Low")

    var title by remember(task.id) { mutableStateOf(task.title) }
    var description by remember(task.id) { mutableStateOf(task.description) }
    var priority by remember(task.id) { mutableStateOf(task.priority) }
    var category by remember(task.id) { mutableStateOf(task.category) }
    var dueDateMillis by remember(task.id) { mutableStateOf(task.dueDate) }

    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    var dueDateStr by remember(task.id) {
        mutableStateOf(
            task.dueDate?.let { dateFormat.format(Date(it)) } ?: ""
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp)
    ) {
        Text(
            text = "Update Task",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        // User-friendly Date & Time Picker Button (DatePickerDialog & TimePickerDialog)
        Button(
            onClick = {
                val calendar = Calendar.getInstance()
                dueDateMillis?.let { calendar.timeInMillis = it }
                DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        TimePickerDialog(
                            context,
                            { _, hourOfDay, minute ->
                                calendar.set(year, month, dayOfMonth, hourOfDay, minute)
                                dueDateMillis = calendar.timeInMillis
                                dueDateStr = dateFormat.format(calendar.time)
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                        ).show()
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text(if (dueDateStr.isBlank()) "Select Due Date & Time" else "Due: $dueDateStr")
        }

        Text(
            text = "Priority",
            modifier = Modifier.padding(top = 16.dp)
        )

        Row(modifier = Modifier.padding(top = 8.dp)) {
            priorities.forEach { item ->
                Text(
                    text = item,
                    color = if (priority == item) Color.White else Color.Black,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .background(
                            if (priority == item) Color.DarkGray
                            else Color.LightGray
                        )
                        .clickable { priority = item }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        TextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        Row(modifier = Modifier.padding(top = 24.dp)) {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(
                            task.copy(
                                title = title.trim(),
                                description = description.trim(),
                                priority = priority,
                                category = category.trim().ifBlank { "Personal" },
                                dueDate = dueDateMillis
                            )
                        )
                    }
                }
            ) {
                Text("Update")
            }

            Button(
                onClick = onCancel,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Cancel")
            }
        }
    }
}
