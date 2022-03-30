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
    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(isRussian = true)
    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(isRussian = false)

    fun getDataFromLocalSource(isRussian: Boolean) {
        Thread {
            liveData.postValue(AppState.Loading)
            if ((1..10).random() > 0) {
                val answer =
                    if (isRussian) repository.getWeatherFromLocalStorageRus() else repository.getWeatherFromLocalStorageWord()
                liveData.postValue(AppState.Success(answer))
            } else
                liveData.postValue(AppState.Error(IllegalAccessException("ошибка получения данных")))

        }.start()
    }
}