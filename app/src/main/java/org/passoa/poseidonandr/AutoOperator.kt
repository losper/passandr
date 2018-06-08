package org.passoa.poseidonandr

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_HOME
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class AutoOperator {
    private var mAccessibilityService: AccessibilityService?=null
    private var mAccessibilityEvent: AccessibilityEvent?=null


    companion object {
        private val mInstance =AutoOperator()
        fun getInstance():AutoOperator{
            return mInstance
        }
    }
    fun init(){
        Log.e("AccessibilityLog","hello world!!!")
    }
    fun updateEvent(service: AccessibilityService?, event: AccessibilityEvent?) {
        if (service != null && mAccessibilityService == null) {
            mAccessibilityService = service
        }
        if (event != null) {
            this.mAccessibilityEvent = event
        }
        MicroService.run()
        var s=mAccessibilityService?.rootInActiveWindow?.packageName.toString()
        MicroService.push("{\"act\":\"notify\",\"id\":\"$s\"}")
        Log.i("updateEvent","{\"act\":\"notify\",\"id\":\"$s\"}")
    }

    fun findNodesByText(s: String): MutableList<AccessibilityNodeInfo>? {
        var list=getRootNodeInfo()?.findAccessibilityNodeInfosByText(s)
        return list
    }
    fun clickByText(s:String){

        var list=getRootNodeInfo()?.findAccessibilityNodeInfosByText(s)
        //Log.e("passoa",s)
        Log.e("NeleusOperatorClick",s)
        list?.forEach {
            var n = it
            while (true) {
                if (n.parent == null || n.isClickable) {
                    break
                } else {
                    //Log.e("NeleusOperatorClick!!!", n.toString())
                    //Log.e("NeleusOperatorClick!!!", "info:" + n.className + n.isClickable)
                    n.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    n = n.parent
                }
            }
            //Log.e("NeleusOperatorClick", n.toString())
            n.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        }
        MicroService.push("{\"act\":\"click\",\"id\":\"$s\"}")
    }
    private fun getRootNodeInfo():AccessibilityNodeInfo? {
        var node:AccessibilityNodeInfo?=null
        node = if (Build.VERSION.SDK_INT>=16){
            mAccessibilityService?.rootInActiveWindow
        }else {
            mAccessibilityEvent?.source
        }
        return node
    }
    fun actionFrom(){
        mAccessibilityService?.performGlobalAction(GLOBAL_ACTION_HOME)
    }
}