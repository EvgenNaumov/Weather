package repository

import com.google.gson.annotations.SerializedName

data class WeatherDTO(
    @SerializedName("fact")
    val fact: FactDTO,
    @SerializedName("forecast")
    val forecastDTO: ForecastDTO,
    @SerializedName("info")
    val infoDTO: InfoDTO,
    @SerializedName("now")
    val now: Int,
    @SerializedName("now_dt")
    val now_dt: String
)