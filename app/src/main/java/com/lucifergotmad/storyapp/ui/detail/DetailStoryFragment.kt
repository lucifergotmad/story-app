package com.lucifergotmad.storyapp.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lucifergotmad.storyapp.R
import com.lucifergotmad.storyapp.databinding.FragmentDetailStoryBinding

class DetailStoryFragment : Fragment() {
    private lateinit var binding: FragmentDetailStoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

}