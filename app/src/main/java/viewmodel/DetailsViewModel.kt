package viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import repository.City
import repository.DetailRepository
import repository.DetailsRepositoryOkHttpimp
import repository.RepositoryImpl

class DetailsViewModel(
    private val liveData: MutableLiveData<DetailsState> = MutableLiveData(),
    private val repository: DetailRepository = DetailsRepositoryOkHttpimp()): ViewModel() {

     fun getWeather(city:City){
         repository.getWeatherDetails(city)
     }


}