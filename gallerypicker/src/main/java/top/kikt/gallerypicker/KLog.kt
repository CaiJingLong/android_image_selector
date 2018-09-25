package top.kikt.gallerypicker

import android.util.Log

fun Any.logi(any: Any?) {

    Log.i("Scanner_${this.javaClass.simpleName}", any?.toString())

}