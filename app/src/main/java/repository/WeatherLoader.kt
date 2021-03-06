package repository


import utils.URL_YANDEX_DOMAIN
import utils.YANDEX_API_KEY
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import android.os.Handler;
import com.example.appweather.BuildConfig
import repository.DTO.WeatherDTO
import repository.ErrorProcessing.ErrorProcessing
import repository.ErrorProcessing.ErrorProcessingImpl
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.util.stream.Collectors

class WeatherLoader {
    private val onServerResponse: OnServerResponse? = null


    fun loadWeather(lat: Double, lon: Double, onServerResponseListener: OnServerResponse) {
        val onErrorProcessing: ErrorProcessing = ErrorProcessingImpl()
        try {//https://
//          val uri: URL = URL("https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon")
            val uri: URL = URL(URL_YANDEX_DOMAIN.plus("lat=$lat&lon=$lon"))

            Thread {
//                lateinit var urlConnection: HttpsURLConnection
                lateinit var urlConnection: HttpURLConnection
                try {


                    urlConnection =
                        (uri.openConnection() as HttpURLConnection).apply {
                            connectTimeout = 1000
                            readTimeout = 1000
                            requestMethod = "GET"
                            addRequestProperty(
                                YANDEX_API_KEY,
                                BuildConfig.WEATHER_API_KEY
                            )
                        }

                    //HW 5
                    val headers = urlConnection.headerFields
                    val responseCode = urlConnection.responseCode
                    val responseMessage = urlConnection.responseMessage

                    var isError: Boolean = false
                    var errString: String = ""
                    try {
//                       onErrorProcessing.onWebApiErrorProcessing(400, "client fail")
                        onErrorProcessing.onWebApiErrorProcessing(responseCode, responseMessage)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        errString = e.message.toString()
                        isError = true
                        Handler(Looper.getMainLooper()).post {
                            onServerResponseListener.onFailed(errString)
                        }
                    } finally {
                        if (isError) {
                            throw IOException(errString)
                        }
                    }
                    //HW 5

                    val result = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    //getLineAsOneBigString(buffer)

                    val weatherDTO: WeatherDTO = Gson().fromJson(result, WeatherDTO::class.java)


                    //object : <> ?????? ?????????????????? ?????????????? object: Runnable{}
                    Handler(Looper.getMainLooper()).post {
                        onServerResponseListener.onResponse(weatherDTO)
                    }
                } catch (e: IOException) {
                    Log.d("@@@@", "loadWeather: Fail connection")
                    Handler(Looper.getMainLooper()).post {
                        onServerResponseListener.onFailed("loadWeather: ${e.message.toString()}")
                    }

                } finally {
                    urlConnection.disconnect()
                }

            }.start()


        } catch (e: MalformedURLException) {
            Log.d("@@@", "loadWeather: fail URI")
            Handler(Looper.getMainLooper()).post {
                onServerResponseListener.onFailed("loadWeather: Fail URI")
            }

        }
    }

    private fun getLineAsOneBigString(buffer: BufferedReader): String {
        return buffer.lines().collect(Collectors.joining("\n"))
    }


}