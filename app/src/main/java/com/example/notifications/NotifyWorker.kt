package com.example.notifications

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.ListenableWorker.Result.success
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlin.random.Random


class NotifyWorker(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {

        createNotificationChannel()
        var builder = NotificationCompat.Builder(context, 0.toString())
            .setSmallIcon(R.drawable.code)
            .setContentTitle("Notification")
            .setContentText("Notify smth")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setChannelId(0.toString())
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(Random.nextInt(), builder.build())
            Log.v("Worker", "notify")
        }

        return success()
    }

    private fun createNotificationChannel() {
        Log.v("Channel", "Create")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = "Notifications Channel"
            val descriptionText = "Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel("0", name, importance)
            mChannel.description = descriptionText

            val notificationManager =
                context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }
}