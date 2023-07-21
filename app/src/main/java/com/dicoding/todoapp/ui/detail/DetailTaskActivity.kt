package com.dicoding.todoapp.ui.detail

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.ui.list.TaskViewModel
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.TASK_ID

class DetailTaskActivity : AppCompatActivity() {

    private lateinit var taskTitle: TextView
    private lateinit var taskDescription: TextView
    private lateinit var taskDueDate: TextView
    private lateinit var deleteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        taskTitle = findViewById(R.id.detail_ed_title)
        taskDescription = findViewById(R.id.detail_ed_description)
        taskDueDate = findViewById(R.id.detail_ed_due_date)
        deleteButton = findViewById(R.id.btn_delete_task)

        val taskId = intent.getIntExtra(TASK_ID, -1)

        if (taskId != -1) {
            val factory = ViewModelFactory.getInstance(this)
            val taskViewModel = ViewModelProvider(this, factory).get(TaskViewModel::class.java)

            taskViewModel.tasks.observe(this) { tasks ->
                val task = tasks.find { it.id == taskId } as? Task
                task?.let {
                    showTaskDetails(it)
                }

                deleteButton.setOnClickListener {
                    task?.let {
                        taskViewModel.deleteTask(it)
                        finish()
                    }
                }
            }
        }
    }

    private fun showTaskDetails(task: Task) {
        taskTitle.text = task.title
        taskDescription.text = task.description
        taskDueDate.text = DateConverter.convertMillisToString(task.dueDateMillis)
    }
}