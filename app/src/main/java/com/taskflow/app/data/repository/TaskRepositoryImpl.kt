package com.taskflow.app.data.repository

import com.taskflow.app.data.local.TaskDao
import com.taskflow.app.data.local.toDomain
import com.taskflow.app.data.local.toEntity
import com.taskflow.app.domain.model.Task
import com.taskflow.app.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(
    private val taskDao: TaskDao
) : TaskRepository {

    override fun getAllTasks(): Flow<List<Task>> =
        taskDao.getAllTasks().map { entities -> entities.map { it.toDomain() } }

    override fun getTaskById(id: Int): Flow<Task?> =
        taskDao.getTaskById(id).map { it?.toDomain() }

    override suspend fun insertTask(task: Task) =
        taskDao.insertTask(task.toEntity())

    override suspend fun updateTask(task: Task) =
        taskDao.updateTask(task.toEntity())

    override suspend fun deleteTask(task: Task) =
        taskDao.deleteTask(task.toEntity())

    override suspend fun updateTaskCompletion(id: Int, isCompleted: Boolean) =
        taskDao.updateTaskCompletion(id, isCompleted)
}