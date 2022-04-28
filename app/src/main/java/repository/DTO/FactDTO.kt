package repository.DTO

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FactDTO(
    @SerializedName("condition")
    val condition: String,
    @SerializedName("daytime")
    val daytime: String,
    @SerializedName("feels_like")
    val feels_like: Int,
    @SerializedName("humidity")
    val humidity: Int,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("obs_time")
    val obs_time: Int,
    @SerializedName("polar")
    val polar: Boolean,
    @SerializedName("pressure_mm")
    val pressure_mm: Double,
    @SerializedName("pressure_pa")
    val pressure_pa: Int,
    @SerializedName("season")
    val season: String,
    @SerializedName("temp")
    val temperature: Int,
    @SerializedName("wind_dir")
    val wind_dir: String,
    @SerializedName("wind_gust")
    val wind_gust: Double,
    @SerializedName("wind_speed")
    val wind_speed: Double
): Parcelable