package com.lucifergotmad.storyapp.ui.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lucifergotmad.storyapp.R
import com.lucifergotmad.storyapp.databinding.FragmentAddStoryBinding

class AddStoryFragment : Fragment() {
    private lateinit var binding: FragmentAddStoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

}