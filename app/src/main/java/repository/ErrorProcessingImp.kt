package repository

import android.util.Log

class ErrorProcessingImp:ErrorProcessing {
    override fun onWebApiErrorProcessing(numError: Int, textErr: String) {

        val info: IntRange = 100..105
        val response: IntRange = 200..226
        val clientside: IntRange = 400..499
        val serverside: IntRange = 500..510

        when (numError){
            in info-> Log.d("@@@_info", "loadWeather: $textErr")
            in clientside-> Log.d("@@@_info", "loadWeather: $textErr")
            in serverside-> Log.d("@@@_info", "loadWeather: $textErr")
            in response-> Log.d("@@@_info", "loadWeather: $textErr")
        }

    }
}