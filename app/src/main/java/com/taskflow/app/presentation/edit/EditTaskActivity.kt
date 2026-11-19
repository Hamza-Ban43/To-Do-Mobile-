
package com.taskflow.app.presentation.edit

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

class EditTaskActivity : ComponentActivity() {

    companion object {
        const val EXTRA_TASK_ID = "task_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val taskId = intent.getIntExtra(EXTRA_TASK_ID, -1)
        val db = TaskDatabase.getInstance(this)

        if (taskId == -1) {
            finish()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val task = db.taskDao().getTaskById(taskId)

            withContext(Dispatchers.Main) {
                if (task == null) {
                    Toast.makeText(
                        this@EditTaskActivity,
                        "Task not found",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    setContent {
                        TaskFlowTheme {
                            EditTaskScreen(
                                task = task,
                                onSave = { updatedTask ->
                                    lifecycleScope.launch(Dispatchers.IO) {
                                        db.taskDao().updateTask(updatedTask)

                                        withContext(Dispatchers.Main) {
                                            Toast.makeText(
                                                this@EditTaskActivity,
                                                "Task updated!",
                                                Toast.LENGTH_SHORT
                                            ).show()
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
        }
    }
}
