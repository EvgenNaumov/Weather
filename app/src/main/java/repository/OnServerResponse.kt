package repository

interface OnServerResponse {
    fun onResponse(weatherDTO:WeatherDTO)
    fun onFailed(infoError:String)
}