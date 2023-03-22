package com.lucifergotmad.storyapp.ui.add

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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

        if (!allPermissionsGranted()) {
            startPermissionRequest()
        }
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