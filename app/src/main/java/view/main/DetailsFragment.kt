package view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.appweather.R
import com.example.appweather.databinding.FragmentDetailsBinding
import com.google.android.material.snackbar.Snackbar
import repository.showSnackbar
import repository.Weather
import repository.createAndShow
import repository.showSnackbar
import viewmodel.AppState
import viewmodel.MainViewModel

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainView:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_main, container, false)
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainView = binding.mainView
        arguments?.getParcelable<Weather>(BUNDLE_WEATHER)?.let { renderData(it) }
    }

    private fun renderData(weather: Weather) {
        binding.apply {
            this.loadingLayout.visibility = View.GONE
            with(weather) {
                cityName.text = city.name
                temperatureValue.text = temperature.toString()
                feelsLikeValue.text = feelsLike.toString()
                cityCoordinates.text = getString(R.string.city_coordinates, city.lat.toString(), city.lon.toString())
//                    "lat: ${city.lat}  lon: ${city.lon}"
            }
        }
        mainView.createAndShow("Оповещение","Успешно",{mainView})
    // "Получилось".showSnackbar(binding.mainView)
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



