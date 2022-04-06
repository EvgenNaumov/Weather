package view.main

import Utils.KEY_BUNDLE_WEATHER
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.appweather.R
import com.example.appweather.databinding.FragmentWeatherListRecyclerItemBinding
import repository.Weather
import repository.createAndShow
import view.MainActivity
import view.weatherlist.onItemListClickListener

class WeatherListAdapter : RecyclerView.Adapter<WeatherListAdapter.MainViewHolder>() {
    private var dataWeather: List<Weather> = listOf()

    private lateinit var onClick: onItemListClickListener

    fun setDataWeather(onItemListClickListener: onItemListClickListener, data: List<Weather>) {
        onClick = onItemListClickListener
        dataWeather = data
        notifyDataSetChanged()//DiffUtils ?
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainViewHolder {
        val binding = FragmentWeatherListRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MainViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: WeatherListAdapter.MainViewHolder, position: Int) {
        holder.bind(dataWeather.get(position))
    }

    override fun getItemCount(): Int {
        return dataWeather.size
    }

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(weather: Weather) {
            //связываем ViewBinding с контекстом itemView
            FragmentWeatherListRecyclerItemBinding.bind(itemView).apply {
                RecyclerItemTextView.text = weather.city.name

                this.RecyclerItemTextView.setOnClickListener {
                    onClick.onItemViewClick(weather)
                }
            }

        }
    }


}