package com.example.application

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import com.example.application.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val CHANNEL_ID = "MAIN_CHANNEL"
        private const val TAG = "MainActivity"
        private const val ACTION_BUTTON_CLICK = "com.example.application.BUTTON_CLICK"
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var networkStateReceiver: NetworkStateReceiver
    private lateinit var buttonReceiver: ButtonClickReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupBinding()
        setupBtnListeners()
        createNotificationChannel()
        setupReceivers()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkStateReceiver)
        unregisterReceiver(buttonReceiver)
    }

    private fun setupBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    // Create and register NetworkStateReceiver, ButtonClickReceiver
    private fun setupReceivers() {
        networkStateReceiver = NetworkStateReceiver()
        registerReceiver(
            networkStateReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        buttonReceiver = ButtonClickReceiver()
        registerReceiver(
            buttonReceiver,
            IntentFilter(ACTION_BUTTON_CLICK)
        )
    }

    private fun setupBtnListeners() {
        binding.btnSendSimple.setOnClickListener {
            sendSimpleNotification()
        }

        binding.btnSendAction.setOnClickListener {
            sendNotificationWithActionButton()
        }

        binding.btnSendReply.setOnClickListener {
            sendNotificationWithReplyButton()
        }

        binding.btnSendProgress.setOnClickListener {
            sendNotificationWithProgressBar()
        }

        binding.btnStartActivity.setOnClickListener {
            CongratulationActivity.start(this, "Happy New Year", 2021)
        }

        binding.btnOpenMap.setOnClickListener {
            val lat = binding.etCoordinateX.text.toString().trim()
            val lng = binding.etCoordinateY.text.toString().trim()

            if (lat.isNotEmpty() && lng.isNotEmpty()) {
                if (isValidLatLng(lat.toDouble(), lng.toDouble())) {
                    val gmmIntentUri = Uri.parse("geo:$lat,$lng")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    startActivity(mapIntent)
                }
            } else {
                Toast.makeText(this, R.string.enter_coordinates, Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBroadcast.setOnClickListener {
            val intent = Intent(ACTION_BUTTON_CLICK)
            sendBroadcast(intent)
        }
    }

    // Validation latitude and longitude
    private fun isValidLatLng(lat: Double, lng: Double): Boolean {
        if (lat < -90 || lat > 90) {
            Toast.makeText(this, R.string.wrong_latitude, Toast.LENGTH_SHORT).show()
            return false
        } else if (lng < -180 || lng > 180) {
            Toast.makeText(this, R.string.wrong_longitude, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.main_channel),
                importance
            ).apply {
                description = getString(R.string.main_channel)
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            Log.d(TAG, "Notification channel created")
        }
    }

    // Build and send Simple Notification
    private fun sendSimpleNotification() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_ac_unit_24)
            .setContentTitle(getString(R.string.simple_notification))
            .setContentText(getString(R.string.simple_notification))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        NotificationManagerCompat.from(this).notify(1, builder.build())
        Log.d(TAG, "Send Simple Notification")
    }

    // Build and send Notification with ActionButton
    private fun sendNotificationWithActionButton() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_ac_unit_24)
            .setContentTitle(getString(R.string.action_notification))
            .setContentText(getString(R.string.action_notification))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .addAction(
                R.drawable.ic_baseline_ac_unit_24,
                getString(R.string.open_app),
                pendingIntent
            )

        NotificationManagerCompat.from(this).notify(2, builder.build())
        Log.d(TAG, "Send Notification with Action button")
    }

    // Build and send Notification with ReplyButton
    private fun sendNotificationWithReplyButton() {
        val intent = Intent(this, NotificationService::class.java)
        val pendingIntent: PendingIntent =
            PendingIntent.getService(applicationContext, 0, intent, 0)

        val remoteInput: RemoteInput = RemoteInput.Builder(NotificationService.EXTRA_TEXT_REPLY)
            .setLabel(getString(R.string.type_message))
            .build()

        val action: NotificationCompat.Action = NotificationCompat.Action.Builder(
            android.R.drawable.ic_menu_send,
            getString(R.string.reply), pendingIntent
        ).addRemoteInput(remoteInput)
            .build()

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_ac_unit_24)
            .setContentTitle(getString(R.string.reply_notification))
            .setContentText(getString(R.string.reply_notification))
            .addAction(action)

        NotificationManagerCompat.from(this).notify(3, builder.build())
        Log.d(TAG, "Send Notification with Reply button")
    }

    // Build and send Notification with ProgressBar
    private fun sendNotificationWithProgressBar() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.progress_notification))
            .setContentText(getString(R.string.download_in_progress))
            .setSmallIcon(R.drawable.ic_baseline_ac_unit_24)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOnlyAlertOnce(true)

        NotificationManagerCompat.from(this).apply {
            Thread {
                var currentProgress = 0
                val maxProgress = 100
                while (currentProgress < maxProgress) {
                    currentProgress += 10
                    builder.setProgress(maxProgress, currentProgress, false)
                    notify(4, builder.build())
                    Thread.sleep(500)
                }

                builder.setContentText(getString(R.string.download_complete))
                    .setProgress(0, 0, false)
                notify(4, builder.build())
            }.start()

        }
        Log.d(TAG, "Send Progress Notification")
    }
}