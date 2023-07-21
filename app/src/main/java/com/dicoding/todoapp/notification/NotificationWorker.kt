package com.dicoding.todoapp.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.data.TaskRepository
import com.dicoding.todoapp.ui.detail.DetailTaskActivity
import com.dicoding.todoapp.utils.NOTIFICATION_CHANNEL_ID
import com.dicoding.todoapp.utils.PreferenceManager
import com.dicoding.todoapp.utils.TASK_ID
import java.util.Calendar
import java.util.concurrent.TimeUnit

class NotificationWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    private val channelName = inputData.getString(NOTIFICATION_CHANNEL_ID)

    private fun getPendingIntent(task: Task): PendingIntent? {
        val intent = Intent(applicationContext, DetailTaskActivity::class.java).apply {
            putExtra(TASK_ID, task.id)
        }
        return TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            } else {
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }
    }

    override fun doWork(): Result {
        val notificationEnabled = PreferenceManager.isNotificationEnabled(applicationContext)

        if (notificationEnabled) {
            val taskRepository = TaskRepository.getInstance(applicationContext)
            val nearestActiveTask = taskRepository.getNearestActiveTask()

            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val pendingIntent = getPendingIntent(nearestActiveTask)

            val notification = NotificationCompat.Builder(applicationContext, channelName ?: "")
                .setContentTitle(nearestActiveTask.title)
                .setContentText(nearestActiveTask.description)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(nearestActiveTask.id, notification)
        }

        scheduleNextWork()

        return Result.success()
    }

    private fun scheduleNextWork() {
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()
        dueDate.timeInMillis = getNearestActiveTaskDueDate()

        if (dueDate.after(currentDate)) {
            val timeDiffMillis = dueDate.timeInMillis - currentDate.timeInMillis
            val delay = timeDiffMillis.coerceAtLeast(0)

            val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(applicationContext).enqueue(workRequest)
        }
    }

    private fun getNearestActiveTaskDueDate(): Long {
        val nearestActiveTaskDueDate = Calendar.getInstance()
        nearestActiveTaskDueDate.add(Calendar.HOUR, 24)

        return nearestActiveTaskDueDate.timeInMillis
    }
}
