package view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.appweather.databinding.FragmentDetailsBinding
import com.google.android.material.snackbar.Snackbar
import repository.Weather
import viewmodel.AppState
import viewmodel.MainViewModel

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_main, container, false)
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weather = arguments?.getParcelable<Weather>(BUNDLE_WEATHER) as Weather
        renderData(weather)
    }

    private fun renderData(weather: Weather) {
        binding.loadingLayout.visibility = View.GONE
        binding.cityName.text = weather?.city.name.toString()
        binding.temperatureValue.text = weather?.temperature.toString()
        binding.feelsLikeValue.text = weather?.feelsLike.toString()
        binding.cityCoordinates.text =
            "${weather?.city.lat} ${weather?.city.lon}"
        Snackbar.make(binding.mainView, "Получилось", Snackbar.LENGTH_LONG).show()
        //Toast.makeText(requireContext(),"РАБОТАЕТ",Toast.LENGTH_SHORT).show()
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



