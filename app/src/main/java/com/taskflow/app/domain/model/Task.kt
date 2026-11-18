
package com.taskflow.app.domain.model

data class Task(
    val id: Int = 0,
    val title: String,
    val description: String = "",
    val priority: Priority = Priority.MEDIUM,
    val category: String = "Personal",
    val dueDate: Long? = null,
    val isCompleted: Boolean = false
)

enum class Priority {
    LOW,
    MEDIUM,
    HIGH
}
