package view.details

import utils.*
import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.appweather.BuildConfig
import com.google.gson.Gson
import repository.ErrorProcessing.ErrorProcessing
import repository.ErrorProcessing.ErrorProcessingImpl
import repository.DTO.WeatherDTO
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class DetailsService(val name: String = "") : IntentService(name) {
    private val message = Intent(KEY_BUNDLE_SERVICE_BROADCAST_WEATHER)

    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            val lat = it.getDoubleExtra(KEY_BUNDLE_LAT, 0.0)
            val lon = it.getDoubleExtra(KEY_BUNDLE_LON, 0.0)
            Log.d(TAG, "onDetailsService: $lat $lon")

            if (lat == 0.0 && lon == 0.0) {
                onEmptyData()
                return
            }

            val onErrorProcessing: ErrorProcessing = ErrorProcessingImpl()
            try {

            val uri: URL = URL(URL_YANDEX_DOMAIN.plus(YANDEX_ENDPOINT).plus("lat=$lat&lon=$lon"))
//                val uri: URL = URL(URL_YANDEX_API.plus(YANDEX_ENDPOINT).plus("lat=$lat&lon=$lon"))

                lateinit var urlConnection: HttpsURLConnection
                try {
                    urlConnection =
                        (uri.openConnection() as HttpsURLConnection).apply {
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

                    onErrorProcessing.onWebApiErrorProcessing(responseCode, responseMessage)

                    val result = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val weatherDTO: WeatherDTO = Gson().fromJson(result, WeatherDTO::class.java)

                    message.putExtra(KEY_BUNDLE_SERVICE_WEATHER, weatherDTO)
                    message.putExtra(DETAILS_ERROR, false)
                    sendBroadcast(message)


                } catch (e: IOException) {
                    Log.d("@@@@", "DetailService: Fail connection")
                    onErrorConnect()
                } catch (e: MyExceptionServer) {
                    Log.d("@@@@", "MyException: $e")
                    onErrorWebAPI(DETAILS_DATA_ERROR_SERVER)
                } catch (e: MyExceptionClient) {
                    Log.d("@@@@", "MyException: $e")
                    onErrorWebAPI(DETAILS_DATA_ERROR_CLIENT)
                }
                finally {
                    urlConnection.disconnect()
                }

            } catch (e: MalformedURLException) {
                Log.d("@@@", "DetailService: fail URI")
                onErrorMalformed()
            }


        }
    }

    private fun onErrorMalformed() {
        putLoadResult(DETAILS_URL_MALFORMED_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(message)
    }

    private fun onErrorConnect(){
        putLoadResult(DETAILS_URL_CONNECTION_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(message)
    }
    private fun onErrorWebAPI(keyMessage:String){
        putLoadResult(keyMessage)
        LocalBroadcastManager.getInstance(this).sendBroadcast(message)
    }

    private fun onEmptyData() {
        putLoadResult(DETAILS_DATA_EMPTY_EXTRA)
        LocalBroadcastManager.getInstance(this).sendBroadcast(message)
    }

    private fun putLoadResult(detailsDataEmptyExtra: String) {
        message.putExtra(KEY_BUNDLE_SERVICE_WEATHER,detailsDataEmptyExtra)
        message.putExtra(DETAILS_ERROR, true)
    }
}