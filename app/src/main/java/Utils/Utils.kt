package Utils

import android.app.Activity
import androidx.room.Room
import com.example.appweather.BuildConfig
import repository.City
import repository.DTO.FactDTO
import repository.Weather
import repository.DTO.WeatherDTO
import repository.getDefaultCity
import room.HistoryEntity
import room.MyDB
import view.main.MyApp

const val WEATHER_APY_KEY           = BuildConfig.WEATHER_API_KEY
const val YANDEX_API_KEY            = "X-Yandex-API-Key"
const val URL_YANDEX_DOMAIN             = "https://api.weather.yandex.ru/"
const val URL_YANDEX_DOMAIN_TEST        = "http://212.86.114.27/"
const val YANDEX_ENDPOINT            = "v2/informers?"
const val KEY_BUNDLE_LAT             = "Lat1"
const val KEY_BUNDLE_LON             = "Lon1"
const val KEY_WAVE                   = "action"
const val KEY_BUNDLE_SERVICE_BROADCAST_WEATHER  = "weather_s_b"
const val KEY_BUNDLE_SERVICE_WEATHER = "key2"
const val KEY_BUNDLE_ACTIVITY_MESSAGE = "key3"
const val TAG                        = "@@@"
const val DETAILS_ERROR              = "key4"
const val KEY_CONNECTION             = "key5"

const val KEY_SP_FILE_NAME_1 = "fileName1"
const val KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN = "is_russian"

class Utils {

}

fun convertDtoToModel(weatherDTO: WeatherDTO): Weather {
    val fact: FactDTO = weatherDTO.fact
    return (Weather(getDefaultCity(), fact.temperature, fact.feels_like, fact.icon))
}

class MyExceptionServer(errorString:String = "",_infoErr:String = ""):Throwable(){
    private var err:String = errorString
    private var _info:String = _infoErr
    override fun toString(): String {
        return _info.plus(" :").plus(err)
    }
}

class MyExceptionClient(errorString:String = "",_infoErr:String = ""):Throwable(){
    private var err:String = errorString
    private var _info:String = _infoErr
    override fun toString(): String {
        return _info.plus(" :").plus(err)
    }
}

fun convertHistoryEntityToWeather(entityList: List<HistoryEntity>): List<Weather> {
    return entityList.map {
        Weather(City(it.city, 0.0, 0.0), it.temperature, it.feelsLike, it.icon) // TODO HW было бы здорово научиться хранить в БД lat lon
    }
}

fun convertWeatherToEntity(weather: Weather): HistoryEntity {
    return HistoryEntity(0, weather.city.name, weather.temperature,weather.feelsLike, weather.icon)
}

fun iniRoomDB(appContext: Activity){

    val db = Room.databaseBuilder(appContext, MyDB::class.java, "test")
        .allowMainThreadQueries() // TODO HW а вам нужно придумать что-то другое
//                            //TODO HW сделать запрос не в главном потоке

        .build()
}
