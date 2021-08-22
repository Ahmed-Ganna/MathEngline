package com.va.mathengline.services

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import com.va.mathengline.models.MathOperationItem
import com.va.mathengline.ui.MainActivity
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import androidx.core.app.NotificationCompat

import android.app.NotificationManager

import android.app.NotificationChannel
import android.content.Context

import android.os.Build
import com.va.mathengline.utils.MathUtils


class EngineService : Service() {
    private val binder = LocalBinder()

    private val worker = Executors.newSingleThreadScheduledExecutor()

    val pendingLveData = MutableLiveData<List<MathOperationItem>>().also {
        it.value = ArrayList()
    }

    val completedLiveData = MutableLiveData<List<MathOperationItem>>().also {
        it.value = ArrayList()
    }

    override fun onCreate() {
        super.onCreate()
        startForeground()
    }

    inner class LocalBinder : Binder() {
        fun getService(): EngineService = this@EngineService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    fun addOperation(operation: MathOperationItem) {
        pendingLveData.add(operation)
        val task = Runnable {
            operation.result = MathUtils().doOperation(operation)
            pendingLveData.remove(operation,true)
            completedLiveData.add(operation,true)
        }
        worker.schedule(task, operation.delaySec, TimeUnit.SECONDS)

    }

    private fun startForeground() {
        if (Build.VERSION.SDK_INT >= 26) {
            val CHANNEL_ID = "my_channel_01"
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Math engine service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                channel
            )
            val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("")
                .setContentText("").build()
            startForeground(1, notification)
        }
    }

}


fun <T> MutableLiveData<List<T>>.add(item: T , post:Boolean = false) {
    val updatedItems = this.value as ArrayList
    updatedItems.add(item)
    if (post) postValue( updatedItems) else value = updatedItems

}


fun <T> MutableLiveData<List<T>>.remove(item: T, post:Boolean = false) {
    val updatedItems = this.value as ArrayList
    updatedItems.remove(item)
    if (post) postValue( updatedItems) else value = updatedItems
}