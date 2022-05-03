package repository

import Utils.TAG
import Utils.URL_YANDEX_DOMAIN
import Utils.convertDtoToModel
import android.util.Log
import com.example.appweather.BuildConfig
import com.google.gson.GsonBuilder
import repository.DTO.WeatherDTO
import repository.ErrorProcessing.ErrorProcessingImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import viewmodel.DetailsViewModel

class DetailsRepositoryRetrofit2Impl:DetailsRepositoryOne {
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

                } else {
                  val codeError = response.code()
                  val errorProcessingImpl = ErrorProcessingImpl()

                  errorProcessingImpl.onWebApiErrorRetrofit(codeError,city.lat,city.lon, object : CallBackAPI{
                      override fun onError(isErrorAPI: Boolean, t: String) {
                          Log.d(TAG,"CallBackAPI")
                          if (isErrorAPI && t!=null){
                              callbackMy.onErrorAPI(t)
                          }
                      }
                  })
                }
            }

            override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                callbackMy.onFail(t)
            }

        })
    }

    interface CallBackAPI{
        fun onError(isErrorAPI:Boolean, t:String)
    }
}