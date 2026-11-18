package com.taskflow.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.taskflow.app.domain.model.Priority
import com.taskflow.app.domain.model.Task

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val priority: String,
    val category: String,
    val dueDate: Long?,
    val isCompleted: Boolean
)

fun TaskEntity.toDomain(): Task = Task(
    id = id,
    title = title,
    description = description,
    priority = Priority.valueOf(priority),
    category = category,
    dueDate = dueDate,
    isCompleted = isCompleted
)

fun Task.toEntity(): TaskEntity = TaskEntity(
    id = id,
    title = title,
    description = description,
    priority = priority.name,
    category = category,
    dueDate = dueDate,
    isCompleted = isCompleted
)