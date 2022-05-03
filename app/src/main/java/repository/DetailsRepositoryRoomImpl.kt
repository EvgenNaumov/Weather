package repository

import Utils.convertHistoryEntityToWeather
import Utils.convertWeatherToEntity
import view.main.MyApp
import viewmodel.DetailsViewModel
import viewmodel.HistoryViewModel

class DetailsRepositoryRoomImpl:DetailsRepositoryOne, DetailsRepositoryAll , DetailsRepositoryAdd{
    override fun getAllWeatherDetails(callback: HistoryViewModel.CallbackForAll) {
        callback.onResponse(convertHistoryEntityToWeather(MyApp.getHistoryDao().getAll()))
    }

    override fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback) {
        val list =convertHistoryEntityToWeather(MyApp.getHistoryDao().getHistoryForCity(city.name))
        if(list.isEmpty()){
            callback.onFail() // то и отобразить нечего
        }else{
            callback.onResponse(list.last()) // FIXME hack
        }
    }

    override fun addWeather(weather: Weather) {
        MyApp.getHistoryDao().insert(convertWeatherToEntity(weather))
    }

}