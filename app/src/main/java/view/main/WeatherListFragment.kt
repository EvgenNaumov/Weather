package view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.appweather.R
import com.example.appweather.databinding.FragmentDetailsBinding
import com.example.appweather.databinding.FragmentWeatherListBinding
import com.google.android.material.snackbar.Snackbar
import repository.Weather
import view.MainActivity
import view.weatherlist.onItemListClickListener
import viewmodel.AppState
import viewmodel.MainViewModel

class WeatherListFragment : Fragment(),onItemListClickListener {

    private var _binding: FragmentWeatherListBinding? = null
    private val binding get() = _binding!!
    private val adapter: WeatherListAdapter = WeatherListAdapter()
    private var isRussiantrue: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_main, container, false)
        _binding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

    }

    private fun initRecyclerView(){

        binding.RecyclerView.adapter = adapter

        val observer = object : Observer<AppState> {
            override fun onChanged(data: AppState) {
                renderData(data)
            }
        }
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getData().observe(viewLifecycleOwner, observer)
        viewModel.getWeatherFromLocalSourceRus()

        binding.mainFragmentFAB.setOnClickListener { changeWeatherDataState(viewModel) }
    }


    private fun changeWeatherDataState(viewModel: MainViewModel) {
        if (isRussiantrue) {
            viewModel.getWeatherFromLocalSourceRus()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        } else {
            viewModel.getWeatherFromLocalSourceWorld()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        }
        isRussiantrue = !isRussiantrue
    }

    private fun renderData(data: AppState) {
        when (data) {
            is AppState.Error -> {
                Snackbar.make(
                    binding.listmainview,
                    "Не получилось ${data.error}",
                    Snackbar.LENGTH_LONG
                ).show()
            }
            is AppState.Loading -> {
            }
            is AppState.Success -> {
                adapter.setDataWeather(this,data.weatherData)
                //Snackbar.make(binding.listmainview, "Получилось", Snackbar.LENGTH_SHORT).show()
                //Toast.makeText(requireContext(),"РАБОТАЕТ",Toast.LENGTH_SHORT).show()
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = WeatherListFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemViewClick(weather: Weather) {
//        Toast.makeText(
//            itemView.context,
//            weather.city.name,
//            Toast.LENGTH_LONG).show()
        val bundle = Bundle()
        bundle.putParcelable(DetailsFragment.BUNDLE_WEATHER,weather)
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.container, DetailsFragment.newInstance(bundle)).addToBackStack("").commit()

    }
}