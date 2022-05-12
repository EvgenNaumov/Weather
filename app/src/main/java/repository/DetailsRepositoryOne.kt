package repository

import android.app.appsearch.BatchResultCallback
import viewmodel.DetailsViewModel

interface DetailsRepositoryOne{
 fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback)
}