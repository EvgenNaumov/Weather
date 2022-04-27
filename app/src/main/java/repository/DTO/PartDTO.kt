package repository.DTO

import com.google.gson.annotations.SerializedName

data class PartDTO(
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
    @SerializedName("part_name")
    val part_name: String,
    @SerializedName("polar")
    val polar: Boolean,
    @SerializedName("prec_mm")
    val prec_mm: Int,
    @SerializedName("prec_period")
    val prec_period: Int,
    @SerializedName("prec_prob")
    val prec_prob: Int,
    @SerializedName("pressure_mm")
    val pressure_mm: Int,
    @SerializedName("pressure_pa")
    val pressure_pa: Int,
    @SerializedName("temp_avg")
    val temp_avg: Int,
    @SerializedName("temp_max")
    val temp_max: Int,
    @SerializedName("temp_min")
    val temp_min: Int,
    @SerializedName("wind_dir")
    val wind_dir: String,
    @SerializedName("wind_gust")
    val wind_gust: Double,
    @SerializedName("wind_speed")
    val wind_speed: Double
)