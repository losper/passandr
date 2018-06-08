package org.passoa.poseidonandr

import android.app.Application

class MyApp:Application() {
    var reqstatus=0
    override fun onCreate() {
        super.onCreate()
        instance=this
    }
    companion object {
        var instance: MyApp? = null
    }
}