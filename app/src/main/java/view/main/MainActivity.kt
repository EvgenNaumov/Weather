package view.main

import Utils.KEY_SP_FILE_NAME_1
import Utils.KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.appweather.R
import view.weatherlist.WeatherListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().let {
                it.replace(R.id.container, WeatherListFragment.newInstance())
                it.commit()
            }

        }

        val sp = getSharedPreferences(KEY_SP_FILE_NAME_1, Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putBoolean(KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN, true)
        editor.apply()

        val defaultValueIsRussian = true
        sp.getBoolean(KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN,defaultValueIsRussian)



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_history->{

            }
        }
        return super.onOptionsItemSelected(item)
    }
}