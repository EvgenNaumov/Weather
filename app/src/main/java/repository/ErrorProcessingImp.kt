package repository

import android.os.Handler
import android.os.Looper
import android.util.Log
import view.main.DetailsFragment
import java.io.IOException

class ErrorProcessingImp(onServerResponseListener: OnServerResponse?) : ErrorProcessing {

    private val info: IntRange = 100..105
    private val response: IntRange = 200..226
    private val clientside: IntRange = 400..499
    private val serverside: IntRange = 500..510
    private val onServerResponseListener = onServerResponseListener
    override fun onWebApiErrorProcessing(numErr: Int, textErr: String, showAllInfo: Boolean) {


        when (numErr) {
            in info -> {
                Log.d("@@@_info", "loadWeather: $textErr")
                if (showAllInfo) {
//                    если нужна вся инфа то в интерфейс onServerResponse добавить функцию
//                    Handler(Looper.getMainLooper()).post {onServerResponseListener?.}
                }
            }
            in clientside -> {
                Log.d("@@@_info", "Ошибка клиента: $textErr")
                throw IOException(textErr)
            }
            in serverside -> {
                Log.d("@@@_info", "Ошибка сервера: $textErr")
                throw IOException(textErr)
            }
            in response -> {
                Log.d("@@@_info", "loadWeather: $textErr")
                if (showAllInfo) {
                    if (showAllInfo) {
//                        если нужна вся инфа то в интерфейс onServerResponse добавить функцию
//                        Handler(Looper.getMainLooper()).post {onServerResponseListener?}
                    }
                }
            }
        }
    }


}