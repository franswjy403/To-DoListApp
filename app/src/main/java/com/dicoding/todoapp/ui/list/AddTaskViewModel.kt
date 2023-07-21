package com.dicoding.todoapp.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.data.TaskRepository
import kotlinx.coroutines.launch

class AddTaskViewModel(private val taskRepository: TaskRepository) : ViewModel() {
    private val _addTaskResult = MutableLiveData<Result<Unit>>()
    val addTaskResult: LiveData<Result<Unit>> get() = _addTaskResult

    fun addTask(title: String, description: String, dueDateMillis: Long) = viewModelScope.launch {
        val newTask = Task(title = title, description = description, dueDateMillis = dueDateMillis)
        try {
            taskRepository.insertTask(newTask)
            _addTaskResult.value = Result.success(Unit)
        } catch (e: Exception) {
            _addTaskResult.value = Result.failure(e)
        }
    }
}