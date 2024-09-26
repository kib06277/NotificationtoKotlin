package com.funshow.notification_kotlin

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RemoteViews
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private val CHANNEL_ID = "Coder"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            // 檢查並請求通知權限
            checkNotificationPermission()
            /**初始化介面控件與點擊事件 */
            val btDefault: Button
            val btCustom: Button
            btDefault = findViewById(R.id.button_DefaultNotification)
            btCustom = findViewById(R.id.button_CustomNotification)
            btDefault.setOnClickListener(onDefaultClick)
            btCustom.setOnClickListener(onCustomClick)
        } catch (e: Exception) {
            Log.i("FunshowError", "e = $e")
        }
    }

    /**點選"系統預設通知" */
    @SuppressLint("MissingPermission")
    private val onDefaultClick = View.OnClickListener {
        try {
            /**建置通知欄位的內容 */
            /**建置通知欄位的內容 */
            val builder = NotificationCompat.Builder(this@MainActivity, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_accessible_forward_24)
                .setContentTitle("哈囉你好！")
                .setContentText("跟你打個招呼啊～")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)

            /**發出通知 */
            /**發出通知 */
            val notificationManagerCompat = NotificationManagerCompat.from(this@MainActivity)
            Log.i("ActivityLog", "POST_NOTIFICATIONS = " + ActivityCompat.checkSelfPermission(this@MainActivity, Manifest.permission.POST_NOTIFICATIONS))

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return@OnClickListener
            }
            notificationManagerCompat.notify(1, builder.build())
        } catch (e: Exception) {
            Log.i("FunshowError", "e = $e")
        }
    }

    /**點選"客製化通知" */
    @SuppressLint("MissingPermission")
    private val onCustomClick = View.OnClickListener {
        try {
            /**建立要嵌入在通知裡的介面 */
            /**建立要嵌入在通知裡的介面 */
            val view = RemoteViews(packageName, R.layout.custom_notification)

            /**初始化Intent，攔截點擊事件 */
            /**初始化Intent，攔截點擊事件 */
            val intent = Intent(this@MainActivity, NotificationReceiver::class.java)
            /**設置通知內"Hi"這個按鈕的點擊事件(以Intent的Action傳送標籤，標籤為Hi) */
            /**設置通知內"Hi"這個按鈕的點擊事件(以Intent的Action傳送標籤，標籤為Hi) */
            intent.action = "Hi"
            val pendingIntent = PendingIntent.getBroadcast(
                this@MainActivity,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            /**設置通知內"Close"這個按鈕的點擊事件(以Intent的Action傳送標籤，標籤為Close) */
            /**設置通知內"Close"這個按鈕的點擊事件(以Intent的Action傳送標籤，標籤為Close) */
            intent.action = "Close"
            val close = PendingIntent.getBroadcast(
                this@MainActivity,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            /**設置通知內的控件要做的事 */
            /**設置通知內的控件要做的事 */
            /*設置標題*/view.setTextViewText(R.id.textView_Title, "哈囉你好！")
            /*設置圖片*/view.setImageViewResource(
                R.id.imageView_Icon,
                R.drawable.ic_baseline_directions_bike_24
            )
            /*設置"Hi"按鈕點擊事件(綁pendingIntent)*/view.setOnClickPendingIntent(
                R.id.button_Noti_Hi,
                pendingIntent
            )
            /*設置"Close"按鈕點擊事件(綁close)*/view.setOnClickPendingIntent(
                R.id.button_Noti_Close,
                close
            )
            /**建置通知欄位的內容 */
            /**建置通知欄位的內容 */
            val builder = NotificationCompat.Builder(this@MainActivity, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_directions_bike_24)
                .setContent(view)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            val notificationManagerCompat = NotificationManagerCompat.from(this@MainActivity)
            Log.i(
                "ActivityLog",
                "POST_NOTIFICATIONS = " + ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            )
            /**發出通知 */
            /**發出通知 */
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@OnClickListener
            }
            notificationManagerCompat.notify(1, builder.build())
        } catch (e: Exception) {
            Log.i("FunshowError", "e = $e")
        }
    }

    // 檢查並請求通知權限
    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 (API 33) or higher
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // 沒有權限，請求權限
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    REQUEST_NOTIFICATION_PERMISSION
                )
            } else {
                // 已經有權限
                Toast.makeText(this, "Notification permission already granted", Toast.LENGTH_SHORT)
                    .show()
                /**檢查手機版本是否支援通知；若支援則新增"頻道" */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(
                        CHANNEL_ID,
                        "DemoCode",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    val manager = getSystemService(
                        NotificationManager::class.java
                    )
                    manager.createNotificationChannel(channel)
                }
            }
        } else {
            // Android 13 以下不需要請求通知權限
            Toast.makeText(
                this,
                "Notification permission not required for this version",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // 處理權限請求結果
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 使用者授予通知權限
                Toast.makeText(this, "Notification permission granted", Toast.LENGTH_SHORT).show()
            } else {
                // 使用者拒絕通知權限
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        // 定義請求通知權限的請求碼
        private const val REQUEST_NOTIFICATION_PERMISSION = 200
    }
}