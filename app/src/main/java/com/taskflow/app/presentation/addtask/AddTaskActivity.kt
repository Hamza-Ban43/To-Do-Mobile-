package com.taskflow.app.presentation.addtask

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.taskflow.app.data.local.TaskDatabase
import com.taskflow.app.ui.theme.TaskFlowTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddTaskActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = TaskDatabase.getInstance(this)

        setContent {
            TaskFlowTheme {
                AddTaskScreen(
                    onSave = { task ->
                        lifecycleScope.launch(Dispatchers.IO) {
                            db.taskDao().insertTask(task)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@AddTaskActivity, "Task saved!", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }
                    },
                    onCancel = { finish() }
                )
            }
        }
    }
}