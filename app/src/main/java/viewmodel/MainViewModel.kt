package viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import repository.RepositoryImpl

class MainViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repository: RepositoryImpl = RepositoryImpl()
) : ViewModel() {

    fun getData(): LiveData<AppState> {
        return liveData
    }

    fun getWeather() {
        Thread {
            liveData.postValue(AppState.Loading)
            if ((0..10).random() > 5){
                val answer = repository.getWeatherFromServer()
                //TODO HW val answer = if(узнать локально или сервер) repository.getWeatherFromServer() else repository.getWeatherFromLocalStorage()
                liveData.postValue(AppState.Success(answer))
            }
            else
                liveData.postValue(AppState.Error(IllegalAccessException()))
        }.start()
    }
}