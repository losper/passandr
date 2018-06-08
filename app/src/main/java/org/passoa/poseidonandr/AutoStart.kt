package org.passoa.poseidonandr

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AutoStart : BroadcastReceiver() {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("passoa","AutoStart")
        val itt = Intent(context, AutoService().javaClass)
        context?.startService(itt)
    }
}