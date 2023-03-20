package com.lucifergotmad.storyapp.ui.detail

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.lucifergotmad.storyapp.core.domain.DetailStory
import com.lucifergotmad.storyapp.core.helper.ViewModelFactory
import com.lucifergotmad.storyapp.databinding.FragmentDetailStoryBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DetailStoryFragment : Fragment() {
    private lateinit var binding: FragmentDetailStoryBinding
    private lateinit var viewModel: DetailStoryViewModel
    private val args: DetailStoryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
    }

    private fun setupViewModel() {
        val factory = ViewModelFactory.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(requireActivity(), factory)[DetailStoryViewModel::class.java]

        viewModel.getUser().observe(viewLifecycleOwner) { result ->
            if (result.token.isNotEmpty()) {
                setupDetailStories(result.token)
            }
        }
    }

    private fun setupDetailStories(token: String) {
        viewModel.getStoryDetail(args.id, "Bearer $token").observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is com.lucifergotmad.storyapp.core.data.Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.root.visibility = View.GONE
                    }
                    is com.lucifergotmad.storyapp.core.data.Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.root.visibility = View.VISIBLE
                        bind(result.data)
                    }
                    is com.lucifergotmad.storyapp.core.data.Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.root.visibility = View.VISIBLE
                        Toast.makeText(
                            requireContext(),
                            "Somethings wrong! " + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun bind(story: DetailStory) {
        binding.apply {
            tvTitle.text = story.name
            tvDescription.text = story.description
            tvCreatedAt.text = story.createdAt
            Glide.with(binding.root)
                .load(story.photoUrl)
                .into((ivItemImage))
        }
    }

}