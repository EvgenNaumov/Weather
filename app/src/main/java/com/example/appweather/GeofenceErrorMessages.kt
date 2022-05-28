package com.example.appweather

import android.content.Context

class GeofenceErrorMessages {
    fun getGeofenceError(context:Context, exception: Exception):String{
        return  exception.message.toString()
    }
}