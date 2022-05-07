package room

import androidx.room.Entity
import androidx.room.PrimaryKey
import repository.City
import repository.getDefaultCity

@Entity(tableName = "history_table")
data class HistoryEntity (
    @PrimaryKey(autoGenerate = true)
    val id:Long,
    var city: String,
    val temperature: Int,
    val feelsLike: Int,
    val icon: String,
    val condition: String = "cloudy",
    val lat:Double,
    val lon:Double

)