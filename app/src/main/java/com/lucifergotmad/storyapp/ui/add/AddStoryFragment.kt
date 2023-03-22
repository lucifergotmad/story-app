package com.lucifergotmad.storyapp.ui.add

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.lucifergotmad.storyapp.databinding.FragmentAddStoryBinding

class AddStoryFragment : Fragment() {
    private lateinit var binding: FragmentAddStoryBinding

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            if (!it.value) {
                Toast.makeText(
                    requireActivity(),
                    "Permission not guaranteed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        if (!allPermissionsGranted()) {
            startPermissionRequest()
        }

        binding.btnCamera.setOnClickListener {
            val toCameraFragment =
                AddStoryFragmentDirections.actionAddStoryFragmentToCameraFragment()
            findNavController().navigate(toCameraFragment)
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
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireActivity(),
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startPermissionRequest() {
        requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
    }

}