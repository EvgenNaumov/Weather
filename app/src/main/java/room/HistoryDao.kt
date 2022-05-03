package room

import androidx.room.*

@Dao
interface HistoryDao {
    annotation class Dao

    @Query("INSERT INTO history_table (city,temperature,feelsLike,icon) VALUES(:city,:temperature,:fellsLike,:icon)")
    fun nativeInsert()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: HistoryEntity)

    @Delete
    fun delete(entity: HistoryEntity)

    @Update
    fun update(entity: HistoryEntity)

    @Query("SELECT * FROM history_table")
    fun getAll():List<HistoryEntity>

    @Query("SELECT * FROM history_table WHERE city=:city")
    fun getHistoryForCity(city:String):List<HistoryEntity>
}