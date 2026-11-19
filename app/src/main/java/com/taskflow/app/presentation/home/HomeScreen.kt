package com.taskflow.app.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taskflow.app.data.local.TaskEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    tasks: List<TaskEntity>,
    onAddClick: () -> Unit,
    onTaskClick: (TaskEntity) -> Unit,
    onDeleteClick: (TaskEntity) -> Unit,
    onToggleCompletion: (TaskEntity, Boolean) -> Unit,
    onShareClick: (TaskEntity) -> Unit
) {
    var taskToDelete by remember { mutableStateOf<TaskEntity?>(null) }
    var selectedFilter by remember { mutableStateOf("All") }
    var selectedSort by remember { mutableStateOf("Default") }

    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    // Dashboard statistics computed from tasks list
    val totalTasks = tasks.size
    val completedTasks = tasks.count { it.isCompleted }
    val pendingTasks = totalTasks - completedTasks
    val completionPercentage = if (totalTasks > 0) (completedTasks * 100) / totalTasks else 0

    // Filter tasks
    val filteredTasks = when (selectedFilter) {
        "Pending" -> tasks.filter { !it.isCompleted }
        "Completed" -> tasks.filter { it.isCompleted }
        else -> tasks
    }

    // Sort tasks
    val sortedTasks = when (selectedSort) {
        "Date" -> filteredTasks.sortedBy { it.dueDate ?: Long.MAX_VALUE }
        "Priority" -> filteredTasks.sortedWith(
            compareBy {
                when (it.priority) {
                    "High" -> 1
                    "Medium" -> 2
                    else -> 3
                }
            }
        )
        else -> filteredTasks.sortedByDescending { it.id }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "TaskFlow",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Button(onClick = onAddClick) {
                Text("Add Task")
            }
        }

        // Dashboard statistics section using simple Text only
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Text(
                text = "Dashboard: Total: $totalTasks | Completed: $completedTasks | Pending: $pendingTasks",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Completion Progress: $completionPercentage%",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Filter Buttons Row (All, Pending, Completed)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("All", "Pending", "Completed").forEach { filter ->
                Button(
                    onClick = { selectedFilter = filter },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedFilter == filter) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.secondaryContainer
                        }
                    )
                ) {
                    Text(filter)
                }
            }
        }

        // Sort Buttons Row (Default, Date, Priority)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Default", "Date", "Priority").forEach { sort ->
                Button(
                    onClick = { selectedSort = sort },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedSort == sort) {
                            MaterialTheme.colorScheme.tertiary
                        } else {
                            MaterialTheme.colorScheme.secondaryContainer
                        }
                    )
                ) {
                    Text("Sort: $sort")
                }
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(sortedTasks, key = { it.id }) { task ->
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

                    // Share Button (Implicit Intent)
                    TextButton(
                        onClick = { onShareClick(task) }
                    ) {
                        Text("Share")
                    }

                    Button(
                        onClick = { taskToDelete = task }
                    ) {
                        Text("X")
                    }
                }
            }
        }
    }

    taskToDelete?.let { task ->
        AlertDialog(
            onDismissRequest = {
                taskToDelete = null
            },
            title = {
                Text("Delete Task?")
            },
            text = {
                Text("Are you sure you want to delete \"${task.title}\"?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteClick(task)
                        taskToDelete = null
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        taskToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        tasks = listOf(
            TaskEntity(
                id = 1,
                title = "Study Android",
                priority = "High",
                category = "Study",
                dueDate = System.currentTimeMillis(),
                isCompleted = false
            ),
            TaskEntity(
                id = 2,
                title = "Buy groceries",
                isCompleted = true
            )
        ),
        onAddClick = {},
        onTaskClick = {},
        onDeleteClick = {},
        onToggleCompletion = { _, _ -> },
        onShareClick = {}
    )
}
