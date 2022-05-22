package com.example.appweather

import android.app.Service
import android.os.Bundle
import com.google.android.gms.common.api.GoogleApiClient

class GeoFenceService() :Service(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener   {
    override fun onConnected(p0: Bundle?) {
        TODO("Not yet implemented")
    }
}
}