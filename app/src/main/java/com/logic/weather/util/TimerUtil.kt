package com.logic.weather.util

import android.os.Message
import java.util.*

class TimerUtil {
    private var timer: Timer = Timer()
    //防抖函数
    fun antiShake(callback:()->Unit, delay: Long = 200) {
        timer.cancel()
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                callback()
            }
        }, delay)
    }
}