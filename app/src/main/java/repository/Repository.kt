package repository

interface Repository {
    fun getWeatherFromServer():Weather
    fun getWeatherFromLocalStorageRus():List<Weather>
    fun getWeatherFromLocalStorageWord():List<Weather>
}