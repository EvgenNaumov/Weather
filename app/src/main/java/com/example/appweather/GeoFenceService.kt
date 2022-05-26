package com.example.appweather

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.Bundle
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.GoogleApi
import com.google.android.gms.common.api.GoogleApiClient

class GeoFenceService(val name:String=""): IntentService(name) {

    override fun onHandleIntent(intent: Intent?) {
        TODO("Not yet implemented")
    }
}
