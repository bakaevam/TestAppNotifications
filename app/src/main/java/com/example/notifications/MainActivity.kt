package com.example.notifications


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.notifications.databinding.ActivityMainBinding
import java.lang.System.currentTimeMillis
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object {
        const val NOTIFICATION_WORK = "NOTIFICATION_WORK"
        const val NOTIFICATION_ID = "NOTIFICATION_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //binding.timePicker.setIs24HourView(true)

        binding.button.setOnClickListener {
            val customCalendar = Calendar.getInstance()
            customCalendar.set(
                    binding.datePicker.year,
                    binding.datePicker.month,
                    binding.datePicker.dayOfMonth,
                    binding.timePicker.hour,
                    binding.timePicker.minute,
                    0
            )

            val customTime = customCalendar.timeInMillis
            val currentTime = currentTimeMillis()
            if(customTime > currentTime) {
                val delay = customTime - currentTime
                Log.v("time custom", "${SimpleDateFormat().format(customCalendar.time).toString()}")
                Log.v("time now", "${SimpleDateFormat().format(currentTime).toString()}")

                scheduleNotification(delay)

                Toast.makeText(this, SimpleDateFormat("HH:MM DD.MM.YYYY").format(customCalendar.time).toString(), LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Incorrect time", LENGTH_LONG).show()
            }
        }
    }

    private fun scheduleNotification(delay: Long) {
        Log.v("schedule", "create notification")
        val request = OneTimeWorkRequestBuilder<NotifyWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build()

        WorkManager
                .getInstance(this)
                .beginUniqueWork(NOTIFICATION_WORK, ExistingWorkPolicy.REPLACE, request)
                .enqueue()
    }
}

