package org.passoa.poseidonandr

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import java.io.*

class AutoService: Service() {
    private val mNotificationManager: NotificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }
    private var mStarted=0
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("passoa","onDestroy")
    }
    private fun sendNotification16(){
        @Suppress("DEPRECATION")
        val nf=Notification.Builder(applicationContext)
                .setContentText("test service for passandr")
                .setContentTitle("passoa")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources,R.mipmap.ic_launcher))
                .setAutoCancel(false)
                .build()
        startForeground(1,nf)
    }
    @TargetApi(Build.VERSION_CODES.O)
    private fun sendNotification26(){
        val id="channel_1"
        val description="123"
        val importance=NotificationManager.IMPORTANCE_LOW
        val mChannel=NotificationChannel(id,description,importance)
        mNotificationManager.createNotificationChannel(mChannel)
        val nf=Notification.Builder(applicationContext,id)
                .setContentText("test service for passandr")
                .setContentTitle("passoa")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(resources,R.mipmap.ic_launcher))
                .setAutoCancel(false)
                .build()
        //mNotificationManager.notify(1,nf)
        startForeground(1,nf)
    }
    private fun sendNotification(){
        if(Build.VERSION.SDK_INT>=26){
            Log.i("passoa","api26")
            sendNotification26()
        }else{
            Log.i("passoa","api16")
            sendNotification16()
        }
    }
    private fun copyAssets(src:String,dst:String){
        try {
            val files=assets.list(src)
            if(files.count()>0){
                File(dst).mkdir()
                for (fn in files) {
                    Log.i("passoa", "$dst/$fn")
                    copyAssets("$src/$fn", "$dst/$fn")
                }
            }else{
                Log.i("passoa", "copy$src to $dst")
                val istream=assets.open(src)
                val ostream= FileOutputStream(dst)
                val buf=ByteArray(1024*6)
                var len:Int
                while (true){
                    len=istream.read(buf)
                    if(len>0){
                        ostream.write(buf,0,len)
                    }else{
                        break
                    }
                }
                istream.close()
                ostream.close()
            }
        }catch (e: IOException){
            e.printStackTrace()
        }
    }
    fun screencap():ByteArray{
        println("screencap!!!")
        return ScreenCap().screencap(
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels
        )
    }
    private fun checkAccessAbility():Boolean{
        return if(AccessUtil.isAccessibilityOn(this)){
            Log.i("passoa","opened")
            true
        }else {
            startActivity( Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            Log.i("passoa","closed")
            false
        }
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("passoa","StartCommand")
        sendNotification()

        if (MyApp.instance?.reqstatus!=0 || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            if(0==mStarted){
                mStarted=1
                Thread({
                    checkAccessAbility()
                    mStarted=0
                }).start()
            }
        }else{
            val inst=Intent(MyApp.instance,Request::class.java)
            inst.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            MyApp.instance?.startActivity(inst)
        }

        return super.onStartCommand(intent, flags, startId)
    }
}