package com.taskflow.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import com.taskflow.app.data.local.TaskDatabase
import com.taskflow.app.data.local.TaskEntity
import com.taskflow.app.presentation.addtask.AddTaskActivity
import com.taskflow.app.presentation.edit.EditTaskActivity
import com.taskflow.app.presentation.home.HomeScreen
import com.taskflow.app.ui.theme.TaskFlowTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    private lateinit var db: TaskDatabase
    private var tasks by mutableStateOf(listOf<TaskEntity>())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        db = TaskDatabase.getInstance(this)
        loadTasks()

        setContent {
            TaskFlowTheme {
                HomeScreen(
                    tasks = tasks,
                    onAddClick = {
                        startActivity(Intent(this, AddTaskActivity::class.java))
                    },
                    onTaskClick = { task ->
                        val intent = Intent(this, EditTaskActivity::class.java)
                        intent.putExtra(EditTaskActivity.EXTRA_TASK_ID, task.id)
                        startActivity(intent)
                    },
                    onDeleteClick = { task ->
                        lifecycleScope.launch(Dispatchers.IO) {
                            db.taskDao().deleteTask(task)
                            withContext(Dispatchers.Main) {
                                tasks = tasks.filterNot { it.id == task.id }
                            }
                        }
                    },
                    onToggleCompletion = { task, isCompleted ->
                        lifecycleScope.launch(Dispatchers.IO) {
                            db.taskDao().updateTaskCompletion(task.id, isCompleted)
                            val updatedList = db.taskDao().getAllTasks()
                            withContext(Dispatchers.Main) {
                                tasks = updatedList
                            }
                        }
                    },
                    onShareClick = { task ->
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                        val dateStr = if (task.dueDate != null) dateFormat.format(Date(task.dueDate)) else "None"
                        val shareText = """
                            TaskFlow Item:
                            Title: ${task.title}
                            Description: ${task.description.ifBlank { "N/A" }}
                            Priority: ${task.priority}
                            Category: ${task.category}
                            Due Date: $dateStr
                            Status: ${if (task.isCompleted) "Completed" else "Pending"}
                        """.trimIndent()

                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        val shareIntent = Intent.createChooser(sendIntent, "Share Task via")
                        startActivity(shareIntent)
                    }
                )
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        loadTasks()
    }

    private fun loadTasks() {
        lifecycleScope.launch(Dispatchers.IO) {
            val result = db.taskDao().getAllTasks()
            withContext(Dispatchers.Main) {
                tasks = result
            }
        }
    }
}
