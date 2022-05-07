package view.historylist

import android.provider.Settings.System.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.bumptech.glide.load.engine.Resource
import com.example.appweather.R
import com.example.appweather.databinding.FragmentHistoryWeatherListRecyclerItemBinding
import repository.Weather

class HistoryWeatherListAdapter(
    private var data: List<Weather> = listOf()
) : RecyclerView.Adapter<HistoryWeatherListAdapter.CityHolder>() {

    fun setData(dataNew: List<Weather>) {
        this.data = dataNew
        notifyDataSetChanged() //DiffUtil кому интересно
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val binding = FragmentHistoryWeatherListRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CityHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        holder.bind(data.get(position))
    }

    override fun getItemCount() = data.size

    inner class CityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(weather: Weather) {
            FragmentHistoryWeatherListRecyclerItemBinding.bind(itemView).apply {
                tvCityName.text = weather.city.name
                tvTemperature.text = weather.temperature.toString()
                tvFeelsLike.text = weather.feelsLike.toString()
                tvLatLon.text = weather.city.lat.toString().plus("/").plus(weather.city.lon.toString())

                //TODO HW вызвать отображение weather.icon
                tvIcon.loadSvg("https://yastatic.net/weather/i/icons/blueye/color/svg/${weather.icon}.svg")
            }
        }
    }

    fun ImageView.loadSvg(url:String){
        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadSvg.context)) }
            .build()
        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(500)
            .data(url)
            .target(this)
            .build()
        imageLoader.enqueue(request)
    }


}
