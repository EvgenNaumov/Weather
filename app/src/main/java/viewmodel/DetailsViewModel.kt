package viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import repository.*

class DetailsViewModel(
    private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
    private val repository: DetailsRepository = DetailsRepositoryOkHttpImpl()): ViewModel() {

    private var repositoryretrofit: DetailsRepository = DetailsRepositoryRetrofit2Impl()

    fun getLiveData() = liveData

    fun getWeather(city: City) {
        liveData.postValue(DetailsState.Loading)
        repositoryretrofit.getWeatherDetails(city, object : Callback {
            override fun onResponse(weather: Weather) {
                liveData.postValue(DetailsState.Success(weather))
            }

            override fun onFail(t:Throwable) {
                liveData.postValue(DetailsState.Error(t.message.toString()))
            }

            override fun onErrorAPI(t: String) {
                liveData.postValue(DetailsState.Error(t)) //("Not yet implemented")
            }
        })


    }

    interface Callback{
        fun onResponse(weather: Weather)

        // TODO HW Fail
        fun onFail(t: Throwable)

        fun onErrorAPI(t:String)
    }
}