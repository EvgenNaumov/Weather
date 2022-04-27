package repository

import Utils.URL_YANDEX_DOMAIN
import Utils.convertDtoToModel
import com.example.appweather.BuildConfig
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import viewmodel.DetailsViewModel

class DetailsRepositoryRetrofit2Impl:DetailsRepository {
    override fun getWeatherDetails(city: City, callbackMy: DetailsViewModel.Callback) {
        val weatherAPI = Retrofit.Builder().apply {
            baseUrl(URL_YANDEX_DOMAIN)
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        }.build().create(WeatherAPI::class.java)

//        weatherAPI.getWeather(WEATHER_API_KEY,city.lat,city.lon).execute() синхронный вызов
        weatherAPI.getWeather(BuildConfig.WEATHER_API_KEY,city.lat,city.lon).enqueue(object :
            Callback<WeatherDTO> {
            override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                if(response.isSuccessful){
                    response.body()?.let {
                        val weather = convertDtoToModel(it)
                        weather.city = city
                        callbackMy.onResponse(weather)
                    }

                }
            }

            override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                //TODO HW
            }

        })
    }
}