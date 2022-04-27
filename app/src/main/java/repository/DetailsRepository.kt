package repository

import android.app.appsearch.BatchResultCallback
import viewmodel.DetailsViewModel

interface DetailsRepository {
 fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback)
}