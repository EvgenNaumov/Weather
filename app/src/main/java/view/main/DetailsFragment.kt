package view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.appweather.R
import com.example.appweather.databinding.FragmentDetailsBinding
import kotlinx.android.synthetic.main.fragment_details.view.*
import repository.*

class DetailsFragment : Fragment(), OnServerResponse {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_main, container, false)
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    lateinit var currentCityName: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainView = binding.mainView

        mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE

        arguments?.getParcelable<Weather>(BUNDLE_WEATHER)?.let {
            currentCityName = it.city.name
            WeatherLoader().loadWeather(it.city.lat, it.city.lon, this@DetailsFragment)
        }
    }

    private fun renderData(weather: WeatherDTO) {
        with(binding) {
            this.mainView.visibility = View.VISIBLE
            this.loadingLayout.visibility = View.GONE

            cityName.text = currentCityName
            temperatureValue.text = weather.fact.temperature.toString()
            feelsLikeValue.text = weather.fact.feels_like.toString()
            cityCoordinates.text = getString(
                R.string.city_coordinates,
                weather.infoDTO.lat.toString(),
                weather.infoDTO.lon.toString()
            )
//                    "lat: ${city.lat}  lon: ${city.lon}"
        }
        mainView.createAndShow("", "Успешно", { mainView })
        // "Получилось".showSnackbar(binding.mainView)
    }

    override fun onFailed(infoErr: String) {
        mainView.mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.GONE
        mainView.createAndShow("", infoErr, { mainView })
    }

    override fun onResponse(weatherDTO: WeatherDTO) {
        renderData(weatherDTO)
    }


    companion object {
        const val BUNDLE_WEATHER: String = "BUNDLE_WEATHER"

        @JvmStatic
        fun newInstance(bundle: Bundle): DetailsFragment {
            val detailsFragment = DetailsFragment()
            detailsFragment.arguments = bundle
            return detailsFragment
        }
    }
}



