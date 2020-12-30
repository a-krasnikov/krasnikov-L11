package com.example.application

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class ButtonClickReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "ButtonClickReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "ButtonClickReceiver - user tap on button")

        Toast.makeText(context, "ButtonClickReceiver - user tap on button", Toast.LENGTH_SHORT)
            .show()
    }
}