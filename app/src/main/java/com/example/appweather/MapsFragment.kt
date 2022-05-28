package com.example.appweather

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.appweather.databinding.FragmentMapsMainBinding
import com.google.android.gms.common.internal.Constants
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import repository.City
import repository.Weather
import repository.createAndShow
import utils.GEOFENCE_EXPIRATION_IN_MILLISECONDS
import utils.TAG
import java.time.Duration
import java.util.*
import kotlin.collections.ArrayList

class MapsFragment : Fragment(), OnCompleteListener<Void> {

    private var _binding: FragmentMapsMainBinding? = null
    private val binding get() = _binding!!
    private var isPermissionGranted: Boolean = false

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->

        map = googleMap
        val moscow = LatLng(55.0, 37.0)
        map.addMarker(MarkerOptions().position(moscow).title("Marker in Moscow"))
        map.moveCamera(CameraUpdateFactory.newLatLng(moscow))
        map.maxZoomLevel
        map.setOnMapLongClickListener {
            addMarkerToArray(it)
            drawLine()
        }

        map.setOnMarkerClickListener { marker ->
            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
            } else {
                marker.showInfoWindow()
            }
            showGeofenceDialog(marker.id, marker.position.latitude, marker.position.longitude)
            false
        }

        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isMapToolbarEnabled = false
        map.uiSettings.isMyLocationButtonEnabled = true

        //внести проверки в ДЗ (открытие карт
        checkPermission()
        context?.let {
            val isPermissionGranted =
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) ==
                        PackageManager.PERMISSION_GRANTED
            map.isMyLocationEnabled = isPermissionGranted
            map.uiSettings.isMyLocationButtonEnabled = isPermissionGranted
        }
    }

    lateinit var geofencingClient: GeofencingClient
    lateinit var geofencePendingIntent: PendingIntent

    val REQUEST_CODE = 111
    private fun GeofencesDialogAdd(stringId: String, lat: Double, lon: Double) {

        val dataGeofence: GeofenceData = GeofenceData()

        GeofenceData.listGeofence.add(
            dataGeofence.getGeofence(
                stringId,
                lat,
                lon,
                GeofenceData.RADIUS
            )
        )
        geofencingClient = LocationServices.getGeofencingClient(requireActivity())

        addGeofences(stringId)
    }

    private fun addGeofences(igG: String) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            var foundId = false
            for (a: Geofence in GeofenceData.listGeofence) {
                if (a.requestId == igG) {
                    foundId = true
                }
            }
            if (foundId) {
                return
            }
            geofencingClient.addGeofences(getGeofencingRequest(), getGfPendingIntent())
                .addOnCompleteListener(this)
        } else {
            view?.let {
                Snackbar.make(
                    requireContext(),
                    it, "недостаточно прав", Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun removeGeofences() {
        if (!checkPermissionGranted()) {
            return
        }
        geofencingClient.removeGeofences(getGfPendingIntent()).addOnCompleteListener(this)
    }

    private fun getGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(GeofenceData.listGeofence)
        }.build()
    }

    private fun getGfPendingIntent(): PendingIntent {
        if (geofencePendingIntent != null) {
            return geofencePendingIntent
        }
        val geoService = Intent(context, GeoFenceService::class.java)
        geofencePendingIntent =
            PendingIntent.getService(context, 0, geoService, PendingIntent.FLAG_UPDATE_CURRENT)
        return geofencePendingIntent
    }

    private fun checkPermissionGranted(): Boolean {
        return isPermissionGranted
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            isPermissionGranted = true
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
                    isPermissionGranted = true
                } else {
                    explain()
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

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

    private fun showGeofenceDialog(markerId: String, lat: Double, lon: Double) {
        activity?.let {
            androidx.appcompat.app.AlertDialog.Builder(it)
                .setTitle(getString(R.string.title_geofences_dialog))
                .setMessage(getString(R.string.dialog_message_geofence))
                .setPositiveButton(getString(R.string.dialog_message_geofence_add)) { _, _ ->
                    GeofencesDialogAdd(markerId, lat, lon)
                }
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
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

        initViewSearchAddres()
    }

    private fun initViewSearchAddres() {
        binding.buttonSearch.setOnClickListener {
            val searchText = binding.searchAddress.text.toString()
            // TODO: сделать проверку  searchText
            if (searchText.isNullOrEmpty()) {
                binding.root.createAndShow(
                    "Результат поиска",
                    "Нечего не найдено. Измените запрос",
                    {})
                return@setOnClickListener
            }
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val results = geocoder.getFromLocationName(searchText, 1)
            // TODO: проверка results
            if (results.size == 0) {
                Snackbar.make(requireContext(),binding.root,"Нечего не найдено. Измените запрос",Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
//                binding.root.createAndShow(
//                    "Результат поиска",
//                    "Нечего не найдено. Измените запрос",
//                    {})
            }
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

    override fun onComplete(task: Task<Void>) {
        Toast.makeText(context, "событие", Toast.LENGTH_SHORT)
        if (task.isSuccessful) {
            Log.d(TAG, "onComplete:${task.result.toString()}")
        } else {
            val errorMessage: String = GeofenceErrorMessages().getGeofenceError(
                requireContext(),
                task.exception as Exception
            )
            Log.d(TAG, "onComplete: $errorMessage")
        }
    }
}