
package com.taskflow.app.domain.repository

import com.taskflow.app.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun getAllTasks(): Flow<List<Task>>

    fun getTaskById(id: Int): Flow<Task?>

    suspend fun insertTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun updateTaskCompletion(
        id: Int,
        isCompleted: Boolean
    )
}
