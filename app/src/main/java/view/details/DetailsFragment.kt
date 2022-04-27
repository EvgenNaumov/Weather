package view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.load
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.example.appweather.R
import com.example.appweather.databinding.FragmentDetailsBinding
import com.squareup.picasso.Picasso
import repository.Weather
import repository.createAndShow
import viewmodel.DetailsState
import viewmodel.DetailsViewModel

const val DETAILS_DATA_EMPTY_EXTRA = "DATA_EMPTY"
const val DETAILS_DATA_ERROR_SERVER = "ERROR SERVER"
const val DETAILS_DATA_ERROR_CLIENT = "ERROR CLIENT"
const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val DETAILS_URL_CONNECTION_EXTRA = "CONNECTION"

class DetailsFragment : Fragment() {
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

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainView = binding.mainView
        mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE

        viewModel.getLiveData().observe(viewLifecycleOwner, object : Observer<DetailsState> {
            override fun onChanged(t: DetailsState) {
                renderData(t)
            }
        })

        arguments?.getParcelable<Weather>(BUNDLE_WEATHER)?.let {
            viewModel.getWeather(it.city)
//            getWeatherRetrofit(it.city.lat, it.city.lon)
        }
    }

    private fun renderData(detailsState: DetailsState) {
        when (detailsState) {
            is DetailsState.Loading -> {
                mainView.visibility = View.GONE
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is DetailsState.Success -> {
                val weather = detailsState.weatherData
                with(binding) {
                    this.mainView.visibility = View.VISIBLE
                    this.loadingLayout.visibility = View.GONE

                    cityName.text = weather.city.name
                    temperatureValue.text = weather.temperature.toString()
                    feelsLikeValue.text = weather.feelsLike.toString()
                    cityCoordinates.text = getString(
                        R.string.city_coordinates,
                        weather.city.lat.toString(),
                        weather.city.lon.toString()
                    )

//                    Glide.with(requireContext())
//                        .load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
//                        .into(headericon)

//                     Picasso.get()?.load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
//                         ?.into(headericon)

                    headericon.load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
                    icon.loadSvg("https://yastatic.net/weather/i/icons/blueye/color/svg/${weather.icon}.svg")

                }
                mainView.createAndShow("", "Успешно", { mainView })
            }
            is DetailsState.Error -> {
                mainView.visibility = View.GONE
                binding.loadingLayout.visibility = View.GONE
                mainView.createAndShow("", "Ошибка", { mainView })
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



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
//        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
//        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(networkStateReciever)
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


