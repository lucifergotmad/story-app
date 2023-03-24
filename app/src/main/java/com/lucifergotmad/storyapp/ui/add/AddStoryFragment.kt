package com.lucifergotmad.storyapp.ui.add

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
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
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lucifergotmad.storyapp.R
import com.lucifergotmad.storyapp.core.helper.ViewModelFactory
import com.lucifergotmad.storyapp.core.helper.rotateBitmap
import com.lucifergotmad.storyapp.core.helper.uriToFile
import com.lucifergotmad.storyapp.databinding.FragmentAddStoryBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class AddStoryFragment : Fragment() {
    private lateinit var binding: FragmentAddStoryBinding
    private lateinit var viewModel: AddStoryViewModel
    private lateinit var accessToken: String
    private var getFile: File? = null

    companion object {
        const val CAMERA_X_REQUEST_KEY = "CAMERA_X_REQUEST_KEY"

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            if (!it.value) {
                Toast.makeText(
                    requireActivity(), "Permission not guaranteed.", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())
            getFile = myFile
            binding.previewImageView.setImageURI(selectedImg)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(CAMERA_X_REQUEST_KEY) { _, bundle ->
            val isBackCamera = bundle.getBoolean("isBackCamera", true)
            val myFile = bundle.getSerializable("picture") as File

            getFile = myFile

            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path), isBackCamera
            )
            binding.previewImageView.setImageBitmap(result)
        }
    }

    private fun uploadImage() {
        if (getFile != null && binding.edtDescription.text?.isNotEmpty() == true) {
            val file = getFile as File
            val description =
                binding.edtDescription.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo", file.name, requestImageFile
            )

            viewModel.addStory(imageMultipart, description, "Bearer $accessToken")
                .observe(viewLifecycleOwner) { result ->
                    if (result != null) {
                        when (result) {
                            is com.lucifergotmad.storyapp.core.data.Result.Loading -> {
                                binding.btnUpload.isEnabled = false
                            }

                            is com.lucifergotmad.storyapp.core.data.Result.Success -> {
                                binding.btnUpload.isEnabled = true
                                Toast.makeText(
                                    requireContext(), result.data, Toast.LENGTH_SHORT
                                ).show()
                                findNavController().navigate(R.id.action_addStoryFragment_to_homeFragment)
                            }

                            is com.lucifergotmad.storyapp.core.data.Result.Error -> {
                                binding.btnUpload.isEnabled = true
                                Toast.makeText(
                                    requireContext(), result.error, Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
        } else {
            Toast.makeText(
                requireContext(),
                "Silakan masukkan berkas gambar atau deskripsi terlebih dahulu.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupViewModel()

        if (!allPermissionsGranted()) {
            startPermissionRequest()
        }

        binding.btnCamera.setOnClickListener {
            val toCameraFragment =
                AddStoryFragmentDirections.actionAddStoryFragmentToCameraFragment()
            findNavController().navigate(toCameraFragment)
        }

        binding.btnGallery.setOnClickListener {
            startGallery()
        }

        binding.btnUpload.setOnClickListener {
            uploadImage()
        }
    }

    private fun setupViewModel() {
        val factory = ViewModelFactory.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(requireActivity(), factory)[AddStoryViewModel::class.java]

        viewModel.getUser().observe(viewLifecycleOwner) { result ->
            if (result.token.isNotEmpty()) {
                accessToken = result.token
            }
        }
    }

    private fun setupView() {
        val window = (requireActivity() as AppCompatActivity).window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            if (window.insetsController != null) {
                window.insetsController?.show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            }
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireActivity(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startPermissionRequest() {
        requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
    }

}