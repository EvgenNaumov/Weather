package repository.ErrorProcessing


import utils.MyExceptionClient
import utils.MyExceptionServer
import android.util.Log
import repository.DetailsRepositoryRetrofit2Impl

class ErrorProcessingImpl() : ErrorProcessing {

    private val infoRange: IntRange = 100..105
    private val responseRange: IntRange = 200..226
    private val clientsideRange: IntRange = 400..499
    private val serversideRange: IntRange = 500..510

    override fun onWebApiErrorProcessing(numErr: Int, textErr: String) {


        when (numErr) {
            in infoRange -> {
                Log.d("@@@_info", "loadWeather: $textErr")
            }
            in clientsideRange -> {
                Log.d("@@@_info", "Ошибка клиента: $textErr")
//                throw MyException(textErr,"CLIENT")
                throw MyExceptionClient("Ошибка клиента:", textErr)
            }
            in serversideRange -> {
                Log.d("@@@_info", "Ошибка сервера: $textErr")
//                throw MyException(textErr,"SERVER")
                throw MyExceptionServer("Ошибка сервера:", textErr)
            }
            in responseRange -> {
                Log.d("@@@_info", "loadWeather: $textErr")
            }
        }
    }

    override fun onWebApiErrorRetrofit(numErr: Int, lat: Double, lon: Double, callbackAPI: DetailsRepositoryRetrofit2Impl.CallBackAPI) {
        var isError:Boolean = false
        if ((lat < -90.0) || (lat > 90.0) || (lon < -180.0) || (lon > 180.0)) {
            isError = true
            callbackAPI.onError(true, "Wrong data (lat/lon) ($numErr)")
        }
        when (numErr) {
            in infoRange -> {
                Log.d("@@@_info", "Информация: $numErr")
                callbackAPI.onError(false, "")
            }
            in clientsideRange -> {
                isError = true
                Log.d("@@@_info", "Ошибка клиента: $numErr")
                callbackAPI.onError(true, "на клиенте ($numErr)")
            }
            in serversideRange -> {
                isError = true
                Log.d("@@@_info", "Ошибка сервера: $numErr")
                callbackAPI.onError(true, "на сервере ($numErr)")
            }
        }
        if ( !isError) {
            callbackAPI.onError(false, "")
        }
    }

}
