package com.example.application

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.RemoteInput

class NotificationService : Service() {

    companion object {
        const val EXTRA_TEXT_REPLY = "TEXT_REPLY"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Toast.makeText(this, "Reply: ${getMessageText(intent)}", Toast.LENGTH_SHORT).show()
        stopSelf()

        return super.onStartCommand(intent, flags, startId)
    }

    private fun getMessageText(intent: Intent?): CharSequence? {
        return RemoteInput.getResultsFromIntent(intent)
            ?.getCharSequence(EXTRA_TEXT_REPLY)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}