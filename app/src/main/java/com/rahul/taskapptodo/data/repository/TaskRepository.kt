package com.rahul.taskapptodo.data.repository

import com.rahul.taskapptodo.data.local.Task
import com.rahul.taskapptodo.data.local.TaskDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun insertTask(task: Task) = taskDao.insertTask(task)

    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

    suspend fun getTaskById(id: Int): Task? = taskDao.getTaskById(id)

    suspend fun markTaskAsCompleted(id: Int) = taskDao.markTaskAsCompleted(id)
}
