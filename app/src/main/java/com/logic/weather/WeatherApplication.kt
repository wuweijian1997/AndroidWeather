package com.logic.weather

import android.app.Application
import android.content.Context

class WeatherApplication: Application() {
    companion object {
        lateinit var context: Context
        const val TOKEN = "15MqIi0rROTmuQWE"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}