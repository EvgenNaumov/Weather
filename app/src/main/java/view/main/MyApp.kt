package view.main

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import room.HistoryDao
import room.MyDB
import utils.URL_YANDEX_DOMAIN

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object {
        private var retrofit:Retrofit? = null
        private var db: MyDB? = null
        private var appContext: MyApp? = null
        fun getHistoryDao(): HistoryDao {
            if (db == null) {
                if (appContext != null) {

                    db = Room.databaseBuilder(appContext!!, MyDB::class.java, "test")
                        //    .allowMainThreadQueries() // TODO HW а вам нужно придумать что-то другое
                        //TODO HW сделать запрос не в главном потоке
                        //в DetailsRepositoryRoomImpl вывозы MyApp.getHistoryDao вынес во внешнии потоки
                        .build()
                } else {
                    throw IllegalStateException("что-то пошло не так, и у нас пустое appContext")
                }
            }
            return db!!.historyDao()
        }

        fun getRetrofit():Retrofit{
            if (retrofit==null){
                if (appContext != null) {
                    retrofit = Retrofit.Builder().apply {
                        baseUrl(URL_YANDEX_DOMAIN)
                        addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                    }.build()
                }
            else{
                    throw IllegalStateException("appContext is null")
                }
            }
            return retrofit!!
        }
        private val migration_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE history_table ADD column condition TEXT NOT NULL DEFAULT ''")
            }

        }
    }


}