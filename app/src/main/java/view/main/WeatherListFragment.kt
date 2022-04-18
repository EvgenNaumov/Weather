package view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.appweather.R
import com.example.appweather.databinding.FragmentWeatherListBinding
import repository.Weather
import repository.showSnackbar
import view.weatherlist.onItemListClickListener
import viewmodel.AppState
import viewmodel.MainViewModel

class WeatherListFragment : Fragment(), onItemListClickListener {

    private var _binding: FragmentWeatherListBinding? = null
    private val binding get() = _binding!!
    private val adapter: WeatherListAdapter = WeatherListAdapter()
    private var isRussiantrue: Boolean = true
    private lateinit var mainView:View

    private val viewModel:MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

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
        mainView = binding.listmainview
        initRecyclerView()

    }

    private fun initRecyclerView() {

        binding.RecyclerView.adapter = adapter

        val observer = object : Observer<AppState> {
            override fun onChanged(data: AppState) {
                renderData(data)
            }
        }
//        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // в эот момент создается view а до этого null
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

                mainView.showSnackbar("Не получилось ${data.error}")
//                Snackbar.make(
//                    binding.listmainview,
//                    "Не получилось ${data.error}",
//                    Snackbar.LENGTH_LONG
//                ).show()
            }
            is AppState.Loading -> {
            }
            is AppState.Success -> {
                adapter.setDataWeather(this, data.weatherData)
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
        requireActivity().supportFragmentManager.beginTransaction().replace(
            R.id.container, DetailsFragment.newInstance(Bundle().apply {
                this.putParcelable(DetailsFragment.BUNDLE_WEATHER, weather)
            })
        ).addToBackStack("").commit()

    }
}