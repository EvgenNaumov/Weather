package view.details

import Utils.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.appweather.R
import com.example.appweather.databinding.FragmentDetailsBinding
import kotlinx.android.synthetic.main.fragment_details.view.*
import repository.*

const val DETAILS_DATA_EMPTY_EXTRA = "DATA_EMPTY"
const val DETAILS_DATA_ERROR_SERVER = "ERROR SERVER"
const val DETAILS_DATA_ERROR_CLIENT = "ERROR CLIENT"
const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val DETAILS_URL_CONNECTION_EXTRA = "CONNECTION"

class DetailsFragment : Fragment(), OnServerResponse {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainView: View
    private lateinit var currentCityName: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_main, container, false)
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: android.content.Context?, intent: Intent?) {
            Log.d(TAG, "onReceive: ")
            intent?.let {
                getWeather(intent)
            }
        }
    }

    //HW6
    private val networkStateReciever = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(TAG, "onReceive: CONNECTIVITY_ACTION")
            var noConnect =
                intent?.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
            if (noConnect == true) {
                onConnectionLast()
                return
            }
            onConnectionFound()
        }
    }

    private fun onConnectionLast() {
        mainView.showSnackbar("Connection lost")
        onError("Connection lost")
    }

    private fun onConnectionFound() {
        mainView.visibility = View.VISIBLE
        binding.loadingLayout.visibility = View.GONE
        mainView.showSnackbar("Connection found")

//        arguments?.getParcelable<Weather>(BUNDLE_WEATHER)?.let {
//            currentCityName = it.city.name
////            WeatherLoader().loadWeather(it.city.lat, it.city.lon, this@DetailsFragment)
//            requireActivity().startService(
//                Intent(
//                    requireContext(),
//                    DetailsService::class.java
//                ).apply {
//                    putExtra(KEY_BUNDLE_LAT, it.city.lat)
//                    putExtra(KEY_BUNDLE_LON, it.city.lon)
//                })
//        }
    }
    //HW6

    private fun getWeather(intent: Intent) {

        if (intent.getBooleanExtra(DETAILS_ERROR, true)) {

            intent.getStringExtra(KEY_BUNDLE_SERVICE_WEATHER).let {
                when (it) {
                    DETAILS_DATA_EMPTY_EXTRA -> {
                        onFailed("Data is empty")
                    }
                    DETAILS_DATA_ERROR_SERVER -> {
                        onError("Error on server")
                    }
                    DETAILS_DATA_ERROR_CLIENT -> {
                        onError("Error on client")
                    }
                    DETAILS_URL_MALFORMED_EXTRA -> {
                        onFailed("URL is error")
                    }
                    DETAILS_URL_CONNECTION_EXTRA -> {
                        onFailed("Connection is error")
                    }
                }

            }

        } else {

            val weatherDTO: WeatherDTO =
                intent.getParcelableExtra<WeatherDTO>(KEY_BUNDLE_SERVICE_WEATHER)!!
            onResponse(weatherDTO)
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainView = binding.mainView
        mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE

        context?.registerReceiver(receiver, IntentFilter(KEY_BUNDLE_SERVICE_BROADCAST_WEATHER))
//        context?.registerReceiver(
//            networkStateReciever,
//            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
//        )


        arguments?.getParcelable<Weather>(BUNDLE_WEATHER)?.let {
            currentCityName = it.city.name
//            WeatherLoader().loadWeather(it.city.lat, it.city.lon, this@DetailsFragment)
            requireActivity().startService(
                Intent(
                    requireContext(),
                    DetailsService::class.java
                ).apply {
                    putExtra(KEY_BUNDLE_LAT, it.city.lat)
                    putExtra(KEY_BUNDLE_LON, it.city.lon)
                })
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
    }

    override fun onFailed(infoError: String) {
        mainView.mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.GONE
        mainView.createAndShow("", infoError, { mainView })
    }

    override fun onError(infoErr: String) {
        mainView.visibility = View.GONE
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(networkStateReciever)
    }

}


