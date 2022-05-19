package com.example.appweather

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.appweather.databinding.FragmentMapsMainBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_maps_main.*
import java.util.*
import kotlin.collections.ArrayList

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsMainBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->

        map = googleMap
        val moscow = LatLng(55.0, 37.0)
        map.addMarker(MarkerOptions().position(moscow).title("Marker in Moscow"))
        map.moveCamera(CameraUpdateFactory.newLatLng(moscow))
        map.setOnMapLongClickListener {
            addMarkerToArray(it)
            drawLine()
        }

        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true

        //внести проверки в ДЗ (открытие карт
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true

        }
    }

    private lateinit var map: GoogleMap
    private val markers: ArrayList<Marker> = arrayListOf()


    private fun addMarkerToArray(location: LatLng) {
        val marker = setMarker(location, markers.size.toString(), R.drawable.ic_map_pin)
        markers.add(marker)
    }

    private fun drawLine() {
        var previousBefore: Marker? = null
        markers.forEach { current ->
            previousBefore?.let { previous ->
                map.addPolyline(
                    PolylineOptions().add(previous.position, current.position)
                        .color(Color.RED)
                        .width(5f)
                )
            }
            previousBefore = current
        }
    }


    private fun setMarker(
        location: LatLng,
        searchText: String,
        resourceId: Int
    ): Marker {
        return map.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText)
                .icon(BitmapDescriptorFactory.fromResource(resourceId))
        )!!
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsMainBinding.inflate(inflater, container, false)
        return binding.root
        //return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        initVoewSearchAddres()

    }

    private fun initVoewSearchAddres() {
        buttonSearch.setOnClickListener {
            val searchText = binding.searchAddress.text.toString()
            // TODO: сделать проверку  searchText
            if (searchText.isEmpty() || searchText.isBlank()) {

            }
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val results = geocoder.getFromLocationName(searchText, 1)
            // TODO: проверка results

            val location = LatLng(results[0].latitude, results[0].longitude)
            val marker = setMarker(
                location,
                searchText,
                R.drawable.ic_map_pin
            )
            map.addMarker(
                MarkerOptions().position(location)
                    .title(searchText)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker))
            )

            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    location, 15f
                )
            )

        }

    }
}