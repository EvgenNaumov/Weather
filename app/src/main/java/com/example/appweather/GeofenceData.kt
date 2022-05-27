package com.example.appweather

import com.google.android.gms.location.Geofence
import utils.GEOFENCE_EXPIRATION_IN_MILLISECONDS

class GeofenceData {


    fun getGeofence(stringId: String, lat: Double, lon: Double, radius:Float): Geofence {
        return Geofence.Builder()
            .setRequestId(stringId)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .setCircularRegion(lat, lon, 200f)
            .setExpirationDuration(GEOFENCE_EXPIRATION_IN_MILLISECONDS)
            .build()
    }

    companion object {
        val listGeofence = mutableListOf<Geofence>()
        const val RADIUS = 1200f
    }
}