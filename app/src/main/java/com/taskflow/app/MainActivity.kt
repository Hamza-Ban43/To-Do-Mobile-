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
import com.taskflow.app.presentation.home.HomeScreen
import com.taskflow.app.ui.theme.TaskFlowTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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