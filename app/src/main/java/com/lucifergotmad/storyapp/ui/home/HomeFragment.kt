package com.lucifergotmad.storyapp.ui.home

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lucifergotmad.storyapp.R
import com.lucifergotmad.storyapp.core.adapter.ListStoryAdapter
import com.lucifergotmad.storyapp.core.helper.ViewModelFactory
import com.lucifergotmad.storyapp.databinding.FragmentHomeBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var listStoryAdapter: ListStoryAdapter
    var accessToken: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupAdapter()
        setupViewModel()
    }

    private fun setupAdapter() {
        listStoryAdapter = ListStoryAdapter()

        binding.listStory.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = listStoryAdapter
        }
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java]

        if (accessToken.isEmpty()) {
            viewModel.getUser().observe(viewLifecycleOwner) { result ->
                Log.d("HomeFragment", "accessToken from viewModel: ${result.token}")
                if (result.token.isNotEmpty()) {
                    accessToken = result.token
                    Log.d("HomeFragment", "accessToken from viewModel2: ${result.token}")
                    setupStories(result.token)
                } else {
                    findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                }
            }
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

    private fun setupStories(token: String) {
        Log.d("HomeFragment", "accessToken: $token")
        viewModel.getStories("Bearer $token").observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is com.lucifergotmad.storyapp.core.data.Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is com.lucifergotmad.storyapp.core.data.Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val listStories = result.data
                        Log.d("HomeFragment", "listStories: $listStories")
                        listStoryAdapter.submitList(listStories)
                    }
                    is com.lucifergotmad.storyapp.core.data.Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Log.d("HomeFragment", "error: ${result.error}")
                        Toast.makeText(
                            context, "Somethings wrong! " + result.error, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}