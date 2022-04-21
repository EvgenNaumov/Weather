package view.details

import Utils.*
import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.appweather.BuildConfig
import com.google.gson.Gson
import repository.ErrorProcessing
import repository.ErrorProcessingImp
import repository.OnServerResponse
import repository.WeatherDTO
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class DetailsService(val name: String = "") : IntentService(name) {

    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            val lat = it.getDoubleExtra(KEY_BUNDLE_LAT,0.0)
            val lon = it.getDoubleExtra(KEY_BUNDLE_LON,0.0)
            Log.d(TAG, "onDetailsService: $lat $lon")


            val onErrorProcessing: ErrorProcessing = ErrorProcessingImp()
            try {
//            val uri: URL = URL("https://api.weather.yandex.ru/v2/informers?lat=$lat&lon=$lon")
                val uri: URL = URL(URL_YANDEX_API.plus(YANDEX_ENDPOINT).plus("lat=$lat&lon=$lon"))

//                Thread {
//                lateinit var urlConnection: HttpsURLConnection
                    lateinit var urlConnection: HttpURLConnection
                    try {


                        urlConnection =
                            (uri.openConnection() as HttpURLConnection).apply {
                                connectTimeout = 1000
                                readTimeout = 1000
                                requestMethod = "GET"
                                addRequestProperty(
                                    WEATHER_API_KEY,
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
                            onErrorProcessing.onWebApiErrorProcessing(responseCode, responseMessage)
                        } catch (e: IOException) {
                            Log.d(TAG, e.printStackTrace().toString())
                            errString = e.message.toString()
                            isError = true
                        } finally {
                            if (isError) {
                                throw MyException(errString)
                            }

                        }
                        //HW 5

                        val result = BufferedReader(InputStreamReader(urlConnection.inputStream))

                        val weatherDTO: WeatherDTO = Gson().fromJson(result, WeatherDTO::class.java)


                        //object : <> это анонимные объекты object: Runnable{}
/*
                        Handler(Looper.getMainLooper()).post {
                            onServerResponseListener.onResponse(weatherDTO)
                        }
*/

                        val message = Intent(KEY_BUNDLE_SERVICE_BROADCAST_WEATHER)
                        message.putExtra(KEY_BUNDLE_SERVICE_WEATHER, weatherDTO)
                        sendBroadcast(message)


                    } catch (e: IOException) {
                        Log.d("@@@@", "DetailService: Fail connection")
//                        Handler(Looper.getMainLooper()).post {
//                            onServerResponseListener.onFailed("loadWeather: ${e.message.toString()}")
//                        }
                    } catch (e:MyException){
                        Log.d("@@@@", "MyException: $e")
//                        Handler(Looper.getMainLooper()).post {
//                            onServerResponseListener.onError("loadWeather: ${e.message.toString()}")
//                        }
                    } finally {
                        urlConnection.disconnect()
                    }

//                }.start()


            } catch (e: MalformedURLException) {
                Log.d("@@@", "DetailService: fail URI")
//                Handler(Looper.getMainLooper()).post {
//                    onServerResponseListener.onFailed("loadWeather: Fail URI")
//                }

            }

        }
    }
}