package viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import repository.*

class DetailsViewModel(
    private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
    private val repositoryAdd: DetailsRepositoryAdd = DetailsRepositoryRoomImpl()
) : ViewModel() {

//    private var repositoryretrofit: DetailsRepositoryOne = DetailsRepositoryRetrofit2Impl()

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

            override fun onFail() {
                //  TODO HW   liveData.postValue(DetailsState.Error()) ("Not yet implemented")
            }

            override fun onErrorAPI(t: String) {
                liveData.postValue(DetailsState.Error(t)) //("Not yet implemented")
            }
        })
    }

    fun isInternet(): Boolean {
        return true
    }


    interface Callback {
        fun onResponse(weather: Weather)

        // TODO HW Fail
        fun onFail()

        fun onErrorAPI(t: String)
    }
}



