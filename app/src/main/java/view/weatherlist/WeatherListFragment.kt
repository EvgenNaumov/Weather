package view.weatherlist

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.appweather.R
import com.example.appweather.databinding.FragmentWeatherListBinding
import repository.City
import repository.Weather
import repository.showSnackbar
import utils.KEY_SP_FILE_NAME_1
import utils.KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN
import view.details.DetailsFragment
import view.main.onItemListClickListener
import viewmodel.AppState
import viewmodel.MainViewModel
import java.util.*

class WeatherListFragment : Fragment(), onItemListClickListener {

    private var _binding: FragmentWeatherListBinding? = null
    private val binding get() = _binding!!
    private val adapter: WeatherListAdapter = WeatherListAdapter()
    private var isRussiantrue: Boolean = true
    private var isRussianCityList: Boolean = true
    private lateinit var mainView: View
    private lateinit var sp: SharedPreferences

    private val viewModel: MainViewModel by lazy {
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
        setupFabLocation()
    }

    private fun setupFabLocation() {
        binding.mainFragmentFABLocation.setOnClickListener {
            checkPermission()
        }
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
        val sp = requireContext().getSharedPreferences(KEY_SP_FILE_NAME_1, Context.MODE_PRIVATE)
        isRussianCityList = sp.getBoolean(KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN, true)
        if (isRussianCityList) {
            viewModel.getWeatherFromLocalSourceRus()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        } else {
            viewModel.getWeatherFromLocalSourceWorld()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        }
        isRussiantrue = !isRussianCityList

        binding.mainFragmentFAB.setOnClickListener { changeWeatherDataState(viewModel) }
    }

    private fun changeWeatherDataState(viewModel: MainViewModel) {
        if (isRussiantrue) {
            viewModel.getWeatherFromLocalSourceRus()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
            isRussianCityList = true
        } else {
            viewModel.getWeatherFromLocalSourceWorld()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
            isRussianCityList = false
        }
        isRussiantrue = !isRussianCityList
        val sp = requireContext().getSharedPreferences(KEY_SP_FILE_NAME_1, Context.MODE_PRIVATE)
        val edit = sp.edit()
        edit.putBoolean(KEY_SP_FILE_NAME_1_KEY_IS_RUSSIAN, isRussianCityList)
        edit.apply()
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

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLocation()
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            explain()
        } else {
            mRequestPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == REQUEST_CODE) {
            for (i in permissions.indices) {
                if (permissions[i] == Manifest.permission.ACCESS_FINE_LOCATION && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    getLocation()
                } else {
                    explain()
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private val REQUEST_CODE = 999
    private fun mRequestPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
    }

    private fun explain() {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_rationale_title))
                .setMessage(getString(R.string.dialog_rationale_meaasge))
                .setPositiveButton(getString(R.string.dialog_rationale_give_access))
                { _, _ ->
                    mRequestPermission()
                }
                .setNegativeButton(getString(R.string.dialog_rationale_decline)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()

        }
    }

    fun getAddressByLocation(location: Location) {
       // val geocoder = Geocoder(requireContext())
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val timeStump = System.currentTimeMillis()
        Thread {
            val addressText = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (addressText.size > 0) {
                addressText[0].getAddressLine(0)
                requireActivity().runOnUiThread {
                    showAddressDialog(addressText[0].getAddressLine(0), location)
                }
            } else {
                requireActivity().runOnUiThread {Toast.makeText(context,"адреса не найдены", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
        Log.d("@@@", " прошло ${System.currentTimeMillis() - timeStump}")
    }

    private val locationListenerTime = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d("@@@", location.toString())
            getAddressByLocation(location)
        }

        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }

        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }

    }

    private val locationListenerDistance = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.d("@@@", location.toString())
            getAddressByLocation(location)
        }

        override fun onProviderDisabled(provider: String) {
            super.onProviderDisabled(provider)
        }

        override fun onProviderEnabled(provider: String) {
            super.onProviderEnabled(provider)
        }

    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        context?.let {
            val locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val providerGPS =
                    locationManager.getProvider(LocationManager.GPS_PROVIDER) // можно использовать BestProvider
                /*providerGPS?.let{
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        10000L,
                        0f,
                        locationListenerTime
                    )
                }*/
                providerGPS?.let {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0,
                        100f,
                        locationListenerDistance
                    )
                }
            }
        }
    }

    private fun showAddressDialog(address: String, location: Location) {
        activity?.let {
            androidx.appcompat.app.AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_address_title))
                .setMessage(address)
                .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _ ->
                    onItemViewClick(
                        Weather(
                            City(
                                address,
                                location.latitude,
                                location.longitude
                            )
                        )
                    )
                }
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
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