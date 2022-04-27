package repository.DTO

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ForecastDTO(
    @SerializedName("date")
    val date: String,
    @SerializedName("date_ts")
    val date_ts: Int,
    @SerializedName("moon_code")
    val moon_code: Int,
    @SerializedName("moon_text")
    val moon_text: String,
//    @SerializedName("partDTOS")
//    val partDTOS: List<PartDTO>,
    @SerializedName("sunrise")
    val sunrise: String,
    @SerializedName("sunset")
    val sunset: String,
    @SerializedName("week")
    val week: Int
):Parcelable