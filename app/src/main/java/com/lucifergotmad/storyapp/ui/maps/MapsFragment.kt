package com.lucifergotmad.storyapp.ui.maps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.lucifergotmad.storyapp.R
import com.lucifergotmad.storyapp.core.domain.Story
import com.lucifergotmad.storyapp.core.helper.ViewModelFactory
import com.lucifergotmad.storyapp.databinding.FragmentMapsBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MapsFragment : Fragment() {
    private lateinit var binding: FragmentMapsBinding
    private lateinit var viewModel: MapsViewModel
    private lateinit var mMap: GoogleMap
    private var accessToken: String? = null

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true

        getMyLocation()
        setMapStyle()
        setStories()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupViewModel()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun setupView() {
        val window = (requireActivity() as AppCompatActivity).window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            if (window.insetsController != null) {
                window.insetsController
                    ?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            }
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(requireActivity(), factory)[MapsViewModel::class.java]

        viewModel.getUser().observe(viewLifecycleOwner) { result ->
            if (result.token.isNotEmpty()) {
                accessToken = result.token
            }
        }
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireContext(),
                        R.raw.map_style
                    )
                )
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    private fun setStories() {
        if (accessToken != null) {
            viewModel.getStoriesLocation("Bearer $accessToken")
                .observe(viewLifecycleOwner) { result ->
                    if (result != null) {
                        when (result) {
                            is com.lucifergotmad.storyapp.core.data.Result.Loading -> {
                                Toast.makeText(
                                    context, "Loading...", Toast.LENGTH_SHORT
                                ).show()
                            }
                            is com.lucifergotmad.storyapp.core.data.Result.Success -> {
                                addManyMarker(result.data)
                            }
                            is com.lucifergotmad.storyapp.core.data.Result.Error -> {
                                Toast.makeText(
                                    context, "Somethings wrong! " + result.error, Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
        }
    }

    private val boundsBuilder = LatLngBounds.Builder()

    private fun addManyMarker(stories: List<Story>) {
        stories.forEach { story ->
            if (story.lat != null && story.lon != null) {
                val latLng = LatLng(story.lat, story.lon)
                mMap.addMarker(
                    MarkerOptions().position(latLng).title(story.name).snippet(story.description)
                )
                boundsBuilder.include(latLng)
            }
        }

        val bounds: LatLngBounds = boundsBuilder.build()

        mMap.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                15
            ),
        )
    }

    companion object {
        const val TAG = "MapsFragment"
    }
}