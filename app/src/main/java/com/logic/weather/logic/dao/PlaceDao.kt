package com.logic.weather.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.logic.weather.WeatherApplication
import com.logic.weather.logic.model.Place

//单例类
object PlaceDao {
    private const val placeKey = "place"
    fun savePlace(place: Place) {
        sharedPreferences().edit {
            putString(placeKey, Gson().toJson(place))
        }
    }

    fun getSavedPlace(): Place {
        val placeJson = sharedPreferences().getString(placeKey, "")
        return Gson().fromJson(placeJson, Place::class.java)
    }

    fun isPlaceSaved() = sharedPreferences().contains(placeKey)

    private fun sharedPreferences() = WeatherApplication.context.getSharedPreferences("weather", Context.MODE_PRIVATE)
}