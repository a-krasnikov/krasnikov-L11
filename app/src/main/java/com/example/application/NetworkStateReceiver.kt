package com.example.application

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.widget.Toast

class NetworkStateReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "NetworkStateReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "Network state changed")

        val conn = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = conn.activeNetworkInfo

        if (networkInfo?.isConnected == true) {
            Toast.makeText(context, R.string.network_available, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, R.string.network_unavailable, Toast.LENGTH_SHORT).show()
        }
    }
}