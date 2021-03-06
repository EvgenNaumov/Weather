package repository

import utils.URL_YANDEX_DOMAIN
import utils.YANDEX_API_KEY
import utils.YANDEX_ENDPOINT
import utils.convertDtoToModel
import com.example.appweather.BuildConfig
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import repository.DTO.WeatherDTO
import viewmodel.DetailsViewModel

class DetailsRepositoryOkHttpImpl : DetailsRepositoryOne {

    override fun getWeatherDetails(city: City, callback: DetailsViewModel.Callback) {

        val client = OkHttpClient()
        val builder = Request.Builder()

        builder.addHeader(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
        builder.url(URL_YANDEX_DOMAIN.plus(YANDEX_ENDPOINT).plus("lat=${city.lat}&lon=${city.lon}"))

        //HW7 синхронный вариант
        val request = builder.build()
        val call: Call = client.newCall(request)
        Thread {
            val response = call.execute()
            if(response.isSuccessful){
                val serverResponse = response.body()!!.string()
                val weatherDTO: WeatherDTO = Gson().fromJson(serverResponse, WeatherDTO::class.java)
                val weather = convertDtoToModel(weatherDTO)
                weather.city = city
                callback.onResponse(weather)
            }else{
                //TODO HW
            }
        }.start()

    }
}