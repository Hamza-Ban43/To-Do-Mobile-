
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taskflow.app.data.local.TaskEntity

@Composable
fun HomeScreen(
    tasks: List<TaskEntity>,
    onAddClick: () -> Unit,
    onTaskClick: (TaskEntity) -> Unit,
    onDeleteClick: (TaskEntity) -> Unit,
    onShareClick: (TaskEntity) -> Unit

) {
    var taskToDelete by remember { mutableStateOf<TaskEntity?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
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

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(tasks, key = { it.id }) { task ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onTaskClick(task) }
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = task.title,
                            fontSize = 18.sp
                        )

                        Text(
                            text = "${task.priority} • ${task.category}",
                            fontSize = 14.sp
                        )
                    }
                    Button(
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
                category = "Study"
            ),
            TaskEntity(id = 2, title = "Buy groceries")
        ),
        onAddClick = {},
        onTaskClick = {},
        onDeleteClick = {},
        onShareClick = {}
    )
}
