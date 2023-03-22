package com.lucifergotmad.storyapp.ui.camera

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lucifergotmad.storyapp.databinding.FragmentCameraBinding

class CameraFragment : Fragment() {
    private lateinit var binding: FragmentCameraBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }
}