package org.passoa.poseidonandr

import kotlin.jvm.internal.Intrinsics
import android.R.attr.data
import android.util.Log


class PackInputStream {
    private var cb:((ByteArray)->Unit)? = null
    fun push(data:ByteArray){
        val head=ByteArray(9)
        var i=0
        head[i++]=0
        head[i++]=0
        head[i++]=1
        head[i++]=data.size.shr(24).toByte()
        head[i++]=data.size.shr(16).toByte()
        head[i++]=data.size.shr(8).toByte()
        head[i++]=data.size.toByte()
        head[i++]=(data.last()+data[0]).toByte()
        head[i]=(head[3]+head[4]+head[5]+head[6]+head[7]).toByte()
        cb?.invoke(head)
        cb?.invoke(data)
    }
    fun onData(callback: (ByteArray) -> Unit){
        cb=callback
    }
}
class PackOutputStream{
    private var tmp:ByteArray?=null
    private var cb:((ByteArray)->Unit)? = null
    private var buf:ByteArray?=null
    private var len=0
    private var end=0

    private fun checkData(data: ByteArray){
        tmp = if (tmp!=null){
            val len=tmp!!.size+data.size
            val x=ByteArray(len)
            System.arraycopy(tmp, 0, x, 0, tmp!!.size)
            System.arraycopy(data, 0, x, tmp!!.size, data.size)
            x
        }else{
            data
        }

    }
    private fun show(data: ByteArray){
        var info=""
        for (v in data){
            info+=" "+v.toString(16)
        }
        Log.i("show",info)
    }
    private fun checkHead():Boolean{
        var sp=2
        var tmp=this.tmp!!

        show(tmp)
        Log.i("passoa","tmp.size:"+tmp.size)

        if(tmp.size<9){
            return false
        }

        while (sp<tmp.size){
            when (tmp[sp]) {
                0.toByte() -> {
                    sp += if (tmp[sp-1]==0.toByte()){
                        2
                    }else {
                        1
                    }
                }
                1.toByte() -> {
                    if(tmp[sp-1]!=0.toByte() || tmp[sp-2]!=0.toByte()){
                        sp+=3
                    }else{
                        this.tmp=tmp.sliceArray(IntRange(sp-2,tmp.size-1))
                        Log.i("checkData","head.."+(sp-2)+""+this.tmp?.size)
                        return true
                    }
                }
                else->{
                    sp+=3
                }
            }
        }
        this.tmp=tmp.sliceArray(IntRange(tmp.size-3,tmp.size))
        return false
    }
    private fun head():Boolean {
        val tmp=this.tmp!!
        if (tmp!!.size > 8) {
            len = (tmp[3].toInt().shl(24))+
                    (tmp[4].toInt().shl(16))+
                    (tmp[5].toInt().shl(8))+
                    tmp[6]
            end = len + 9
            return true
        }
        return false
    }
    private fun checkCrc():Boolean{
        var tmp=this.tmp!!
        if((tmp[3] + tmp[4] + tmp[5] + tmp[6] + tmp[7]).toByte() == tmp[8]){
            return true
        }
        this.tmp=tmp.sliceArray(IntRange(3,tmp.size))
        return false
    }
    private fun parse():Boolean{
        var tmp=this.tmp!!
        Log.i("error4",""+end+""+tmp.size)
        if(end>tmp.size){
            return false
        }
        if(end==tmp.size){
            buf=tmp.sliceArray(IntRange(9,tmp.size))
            this.tmp=null
        }else{
            buf=tmp.sliceArray(IntRange(9,end))
            this.tmp=tmp.sliceArray(IntRange(end,tmp.size))
        }
        this.len=0
        this.end=0
        return true
    }
    private fun next():Boolean{
        if (tmp==null){
            return false
        }

        if (!checkHead()){
            Log.e("passoa","error1")
            return false
        }
        if (!head()){
            Log.e("passoa","error2")
            return false
        }
        if(!checkCrc()){
            Log.e("passoa","error3")
            return false
        }
        if(!parse()){
            Log.e("passoa","error4")
            return false
        }
        return true
    }
    fun push(data: ByteArray){
        checkData(data)
        while (next()){
            cb?.invoke(buf!!)
        }
    }
    fun onData(callback: (ByteArray) -> Unit){
        cb=callback
    }
}