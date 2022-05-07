package viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import repository.DetailsRepositoryRoomImpl
import repository.Weather

class HistoryViewModel(private val liveData: MutableLiveData<AppState> = MutableLiveData(),
                       private val repository: DetailsRepositoryRoomImpl = DetailsRepositoryRoomImpl() ):ViewModel() {
    fun getData(): LiveData<AppState> {
        return liveData
    }

    fun getAll(){
         repository.getAllWeatherDetails(object :CallbackForAll{
            override fun onResponse(listWeather: List<Weather>) {
                liveData.postValue(AppState.Success(listWeather))
            }

            override fun onFail(t:Throwable) {
                liveData.postValue(AppState.Error(t))
            }

        })
    }

    interface CallbackForAll {
        fun onResponse(listWeather: List<Weather>)
        // TODO HW Fail
        fun onFail(t:Throwable)
    }

}