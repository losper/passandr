package org.passoa.poseidonandr

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle

class Request:Activity() {
    private val REQUEST_MEDIA_PROJECTION = 18
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission()
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun requestPermission() {
        val mpm= getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityForResult(
                mpm.createScreenCaptureIntent(),
                REQUEST_MEDIA_PROJECTION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_MEDIA_PROJECTION->{
                moveTaskToBack(true)
                MyApp.instance?.reqstatus=1
                val itt = Intent(applicationContext, AutoService().javaClass)
                applicationContext.startService(itt)
            }
            else->{
                println("exit")
            }
        }
    }
}