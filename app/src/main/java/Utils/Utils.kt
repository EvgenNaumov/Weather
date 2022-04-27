package Utils

import com.example.appweather.BuildConfig
import repository.FactDTO
import repository.Weather
import repository.WeatherDTO
import repository.getDefaultCity

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

class Utils {

}

fun convertDtoToModel(weatherDTO: WeatherDTO): Weather {
    val fact: FactDTO = weatherDTO.fact
    return (Weather(getDefaultCity(), fact.temperature, fact.feels_like, fact.icon))
}

class MyExceptionServer(_err:String = "",_infoErr:String = ""):Throwable(){
    private var err:String = _err
    private var _info:String = _infoErr
    override fun toString(): String {
        return _info.plus(" :").plus(err)
    }
}

class MyExceptionClient(_err:String = "",_infoErr:String = ""):Throwable(){
    private var err:String = _err
    private var _info:String = _infoErr
    override fun toString(): String {
        return _info.plus(" :").plus(err)
    }
}
