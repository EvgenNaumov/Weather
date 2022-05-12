package viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import repository.*

class DetailsViewModel(
    private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
    private val repositoryAdd: DetailsRepositoryAdd = DetailsRepositoryRoomImpl()
) : ViewModel() {

    private var noConnect:Boolean = true
    fun getLiveData() = liveData

    fun getWeather(city: City) {
        liveData.postValue(DetailsState.Loading)
        val repositoryOne = if (isInternet()) {
            DetailsRepositoryRetrofit2Impl()
        } else {
            DetailsRepositoryRoomImpl()
        }

        repositoryOne.getWeatherDetails(city, object : Callback {
            override fun onResponse(weather: Weather) {
                liveData.postValue(DetailsState.Success(weather))
                if (isInternet()) {
                    repositoryAdd.addWeather(weather)
                }
            }

            override fun onFail(t:String) {
                liveData.postValue(DetailsState.Error(t))
            }

            override fun onErrorAPI(t: String) {
                liveData.postValue(DetailsState.Error(t)) //("Not yet implemented")
            }
        })
    }

    private fun isInternet(): Boolean {
        return noConnect
    }

    interface Callback {
        fun onResponse(weather: Weather)

        // TODO HW Fail
        fun onFail(t:String)

        fun onErrorAPI(t: String)
    }

}



