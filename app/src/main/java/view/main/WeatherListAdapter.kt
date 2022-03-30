package view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.appweather.databinding.FragmentWeatherListRecyclerItemBinding
import repository.Weather

class WeatherListAdapter : RecyclerView.Adapter<WeatherListAdapter.MainViewHolder>() {
    private var dataWeather: List<Weather> = listOf()

    fun setDataWeather(data: List<Weather>) {
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
            val binding = FragmentWeatherListRecyclerItemBinding.bind(itemView)
            binding.RecyclerItemTextView.text = weather.city.name
            binding.RecyclerItemTextView.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    weather.city.name,
                    Toast.LENGTH_LONG).show()
            }

        }
    }

}