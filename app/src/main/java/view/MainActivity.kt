package view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appweather.R
import view.main.WeatherListFragment

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
    }
}