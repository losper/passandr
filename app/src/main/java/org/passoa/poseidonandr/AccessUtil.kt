package org.passoa.poseidonandr

import android.content.Context
import android.provider.Settings
import android.text.TextUtils
import android.util.Log

class AccessUtil {
    companion object {
        fun isAccessibilityOn(context: Context): Boolean {
            var accessibilityEnabled = 0
            try {
                accessibilityEnabled = Settings.Secure.getInt(context.applicationContext.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
            } catch (e: Settings.SettingNotFoundException) {
                e.printStackTrace()
            }

            if (accessibilityEnabled == 1) {
                val list = TextUtils.SimpleStringSplitter(':')
                val serviceName = context.packageName+"/"+AutoAccessService::class.java.canonicalName
                val settingVal= Settings.Secure.getString(context.applicationContext.contentResolver,Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
                var str:String
                Log.e("passoa", "settingVal:$settingVal")
                list.setString(settingVal)
                Log.i("passoa", "serviceName:$serviceName")
                while (list.hasNext()){
                    str =list.next()
                    Log.i("passoa", "str:$str")
                    if (str.equals(serviceName,true)){
                        return true
                    }
                }
                return false
            }
            return false
        }
    }
}