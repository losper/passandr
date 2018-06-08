package org.passoa.poseidonandr

import android.util.Log
import com.koushikdutta.async.AsyncServer
import com.koushikdutta.async.AsyncSocket
import com.koushikdutta.async.ByteBufferList
import org.json.JSONObject


object MicroService {
    private var cli:AsyncSocket?=null
    private var bInit=false
    private val pis=PackInputStream()
    private val pos=PackOutputStream()
    init {
        pis.onData { data->cli?.write(ByteBufferList(data)) }
        pos.onData { data-> this.onMsg(data) }
    }
    private fun onData(bb: ByteBufferList){
        Log.i("onData",""+bb.allByteArray.size)
        pos.push(bb.allByteArray)
    }
    private fun onMsg(data:ByteArray){
        val str=data.toString(charset("utf-8"))
        Log.i("passoa","onMsg"+str)
        val obj=JSONObject(str)
        when(obj.getString("act")){
            "click"->{AutoOperator.getInstance().clickByText(obj.getString("id"))}
        }
    }
    private fun onClosed(ex: Exception?) {
        Log.e("passoa",ex.toString())
    }

    private fun onConnect(ex:Exception?, socket:AsyncSocket?){
        cli=socket
        cli?.setClosedCallback({ _ -> onClosed(ex)})
        cli?.setDataCallback({ _, bb->onData(bb)})
        cli?.setEndCallback { _->onEnd(ex) }
        if(ex!=null){
            Log.e("passoa",ex.message)
            asyncSocket()
        }
    }

    private fun onEnd(ex: Exception?) {
        Log.e("passoa",ex.toString())
    }

    private fun asyncSocket(){
        AsyncServer.getDefault().connectSocket("192.168.0.24",8009,{ex, socket -> onConnect(ex,socket) })
    }
    fun run(){
        if(!bInit) {
            bInit=true
            asyncSocket()
        }
    }

    fun push(buf: ByteArray) {
        pis.push(buf)
    }
    fun push(buf: String) {
        pis.push(buf.toByteArray())
    }
}