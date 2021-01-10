package com.logic.weather

import android.app.Application
import android.content.Context

class WeatherApplication: Application() {
    companion object {
        lateinit var context: Context
        const val TOKEN = "天气令牌"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}