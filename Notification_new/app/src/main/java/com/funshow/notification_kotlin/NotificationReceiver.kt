package com.funshow.notification_kotlin

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        /**接收點擊事件 */
        when (intent.action) {
            "Hi" -> Toast.makeText(context, "哈囉！", Toast.LENGTH_SHORT).show()
            "Close" -> {
                val manager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.cancel(1)
            }
        }
    }
}