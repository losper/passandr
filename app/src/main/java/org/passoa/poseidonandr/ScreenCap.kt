package org.passoa.poseidonandr

import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import java.io.ByteArrayOutputStream

class ScreenCap{

    fun screencap(w:Int,h:Int):ByteArray{
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //5.0 之后才允许使用屏幕截图
            screencap1(w,h)
        }else{
            screencap2(w,h)
        }
    }


    private fun screencap2(w: Int, h: Int): ByteArray {


        val asc = byteArrayOf(32,33,34,35,36)
        return asc
    }
    private fun screencap1(w:Int, h:Int):ByteArray{
        println("screencap!!!")
        if(Build.VERSION.SDK_INT in 17..18) {
            val os = ByteArrayOutputStream()
            val sc = Class.forName("android.view.Surface")
            val method = sc.getMethod("screenshot", Integer.TYPE, Integer.TYPE)
            val o = method.invoke(sc, w, h)
            Log.i("passoa", "check:" + o.toString())
            val mScreenBitmap = o as Bitmap
            Log.i("passoa", "check:" + mScreenBitmap.byteCount)
            mScreenBitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
            mScreenBitmap.recycle()
            os.flush()
            os.close()
            return os.toByteArray()
        }else{
            return "123".toByteArray()
        }
    }
}