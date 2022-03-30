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
import viewmodel.AppState
import viewmodel.MainViewModel

class DetailsFragment : Fragment() {

    private var _binding:FragmentDetailsBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_main, container, false)
        _binding = FragmentDetailsBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val observer = object : Observer<AppState> {
            override fun onChanged(data: AppState) {
                renderData(data)
            }
        }
        viewModel.getData().observe(viewLifecycleOwner,observer)



        val switcher:Switch = binding.switch1
        switcher.setOnCheckedChangeListener{buttonView, isChecked->
/*
            if (isChecked){
                viewModel.getWeather(true)
            }else{
                viewModel.getWeather(false)
            }
*/

        }
    }

    private fun renderData(data: AppState) {
        when (data){
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Snackbar.make(binding.mainView, "Не получилось ${data.error}", Snackbar.LENGTH_LONG).show()
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                binding.cityName.text = data.weatherData[0]?.city.name.toString()
                binding.temperatureValue.text = data.weatherData[0]?.temperature.toString()
                binding.feelsLikeValue.text = data.weatherData[0]?.feelsLike.toString()
                binding.cityCoordinates.text = "${data.weatherData[0]?.city.lat} ${data.weatherData[0]?.city.lon}"
                Snackbar.make(binding.mainView, "Получилось", Snackbar.LENGTH_LONG).show()
                //Toast.makeText(requireContext(),"РАБОТАЕТ",Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = DetailsFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
       _binding = null
    }
}