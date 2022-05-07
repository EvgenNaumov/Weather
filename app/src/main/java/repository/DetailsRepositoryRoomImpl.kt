package repository

import utils.convertHistoryEntityToWeather
import utils.convertWeatherToEntity
import view.main.MyApp
import viewmodel.DetailsViewModel
import viewmodel.HistoryViewModel

class DetailsRepositoryRoomImpl : DetailsRepositoryOne, DetailsRepositoryAll, DetailsRepositoryAdd {
    override fun getAllWeatherDetails(callback: HistoryViewModel.CallbackForAll) {

        Thread {
            callback.onResponse(convertHistoryEntityToWeather(MyApp.getHistoryDao().getAll()))
        }.start()
//        lateinit var listHistory: List<HistoryEntity>
//        val handlerThread = HandlerThread(R.string.myThreadDB.toString())
//        handlerThread.start()
//        var handler = Handler(handlerThread.looper)
//        handler.post(Runnable {
//            listHistory = MyApp.getHistoryDao().getAll()
//            callback.onResponse(convertHistoryEntityToWeather(listHistory))
//        })
//        handlerThread.quitSafely()

    }

    override fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback) {

        Thread {
            val list = convertHistoryEntityToWeather(MyApp.getHistoryDao().getHistoryForCity(city.name))
            if (list.isEmpty()) {
                callback.onFail("") // то и отобразить нечего
            } else {
                callback.onResponse(list.last()) // FIXME hack
            }
        }.start()

//        lateinit var listHistory: List<HistoryEntity>
//        val handlerThread = HandlerThread(R.string.myThreadDB.toString())
//        handlerThread.start()
//        var handler = Handler(handlerThread.looper)
//        handler.post(Runnable { listHistory = MyApp.getHistoryDao().getHistoryForCity(city.name)
//            val list = convertHistoryEntityToWeather(listHistory)
//            if (list.isEmpty()) {
//                callback.onFail() // то и отобразить нечего
//            } else {
//                callback.onResponse(list.last()) // FIXME hack
//            }
//
//        })
//        handlerThread.quitSafely()


    }

    override fun addWeather(weather: Weather) {

        Thread{
            MyApp.getHistoryDao().insert(convertWeatherToEntity(weather))
        }.start()
//        val handlerThread = HandlerThread(R.string.myThreadDB.toString())
//        handlerThread.start()
//        var handler = Handler(handlerThread.looper)
//        handler.post(Runnable { MyApp.getHistoryDao().insert(convertWeatherToEntity(weather)) })
//        handlerThread.quitSafely()
    }

}