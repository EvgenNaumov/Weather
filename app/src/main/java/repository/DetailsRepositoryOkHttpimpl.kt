package repository

import Utils.URL_YANDEX_API
import Utils.WEATHER_API_KEY
import Utils.YANDEX_ENDPOINT
import com.example.appweather.BuildConfig
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class DetailsRepositoryOkHttpimp:DetailRepository {
    override fun getWeatherDetails(city: City): WeatherDTO {

        val client = OkHttpClient()
        val builder = Request.Builder()

        builder.addHeader(WEATHER_API_KEY, BuildConfig.WEATHER_API_KEY)
        builder.url(URL_YANDEX_API.plus(YANDEX_ENDPOINT).plus("lat=$lat&lon=$lon"))

        //HW7 ассихронный вариант
        val request = builder.build()
        val callBack: Callback = object : Callback {
            override fun onFailure(call: Call, e: IOException) {

            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val weatherDTO: WeatherDTO =
                        Gson().fromJson(response.body()!!.string(), WeatherDTO::class.java)
                    requireActivity().runOnUiThread { onResponse(weatherDTO) }

                } else {

                }

            }

        }

        val call: Call = client.newCall(request)
        call.enqueue(callBack)
    }
}