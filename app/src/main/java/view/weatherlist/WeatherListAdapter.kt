package view.weatherlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appweather.databinding.FragmentWeatherListRecyclerItemBinding
import repository.Weather
import view.main.onItemListClickListener

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

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
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