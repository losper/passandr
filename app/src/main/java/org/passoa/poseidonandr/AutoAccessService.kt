package org.passoa.poseidonandr

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class AutoAccessService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        //val pkgName= event?.packageName.toString()
        //val type= event?.eventType
        AutoOperator.getInstance().updateEvent(this,event)
        return
        /*when (type) {
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
                ,AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                NeleusAccessibilityOperator.getInstance().updateEvent(this,event)
            }
        }*/
    }

    override fun onInterrupt() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    public override fun onServiceConnected() {
        super.onServiceConnected()
    }

}