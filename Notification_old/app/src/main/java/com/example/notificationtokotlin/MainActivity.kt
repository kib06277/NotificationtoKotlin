package com.example.notificationtokotlin

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var button: Button
    var isOpened = false
    var channelId = "channel1"
    var channelName = "name"
    var Manager: NotificationManager? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            Manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            button = findViewById(R.id.btn_Notify)
            button.setOnClickListener(View.OnClickListener {
                isOpened = Manager!!.areNotificationsEnabled()
                if (!isOpened) {
                    Toast.makeText(this@MainActivity, "請開啟通知權限", Toast.LENGTH_SHORT).show()
                    val intent = Intent()
                    intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, this@MainActivity.packageName)
                    intent.putExtra(
                        Settings.EXTRA_CHANNEL_ID,
                        this@MainActivity.applicationInfo.uid
                    )
                    this@MainActivity.startActivity(intent)
                }
                var channel: NotificationChannel? = null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
                    Manager!!.createNotificationChannel(channel)
                    val builder = Notification.Builder(this@MainActivity, channelId)
                    builder.setSmallIcon(R.mipmap.ic_launcher)
                        .setChannelId(channelId)
                        .setContentTitle("標題")
                        .setContentText("內容").build()
                    Manager!!.createNotificationChannel(channel)
                    Manager!!.notify(1, builder.build())
                }
            })
        } catch (e: Exception) {
            Log.i("Error", "e = $e")
        }
    }
}