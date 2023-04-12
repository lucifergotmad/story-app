package com.lucifergotmad.storyapp.ui.home

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.MenuProvider
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lucifergotmad.storyapp.R
import com.lucifergotmad.storyapp.core.adapter.ListStoryAdapter
import com.lucifergotmad.storyapp.core.adapter.LoadingStateAdapter
import com.lucifergotmad.storyapp.core.helper.ViewModelFactory
import com.lucifergotmad.storyapp.databinding.FragmentHomeBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var listStoryAdapter: ListStoryAdapter

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
        setupMenu()
        setupAdapter()
        setupViewModel()
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.profile_menu -> {
                        val toProfileFragment =
                            HomeFragmentDirections.actionHomeFragmentToProfileFragment()
                        findNavController().navigate(toProfileFragment)
                        true
                    }
                    R.id.maps_menu -> {
                        val toMapsFragment =
                            HomeFragmentDirections.actionHomeFragmentToMapsFragment()
                        findNavController().navigate(toMapsFragment)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupAdapter() {
        listStoryAdapter = ListStoryAdapter()
        listStoryAdapter.setOnItemClickCallback(object :
            ListStoryAdapter.OnItemClickCallback {
            override fun onItemClicked(id: String) {
                moveToDetailPage(id)
            }
        })

        binding.listStory.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = listStoryAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    listStoryAdapter.retry()
                }
            )
        }
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java]

        viewModel.getThemeSettings().observe(
            viewLifecycleOwner
        ) { isDarkModeActive: Boolean ->
            binding.apply {
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }

        viewModel.getUser().observe(viewLifecycleOwner) { result ->
            if (result.token.isNotEmpty()) {
                setupStories(result.token)
            } else {
                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
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

        binding.floatingActionButton.setOnClickListener {
            moveToAddPage()
        }
    }

    private fun setupStories(token: String) {
        viewModel.getStoriesPaging("Bearer $token").observe(viewLifecycleOwner) { result ->
            Log.d("HomeFragment", result.toString())
            if (result != null) {
                listStoryAdapter.submitData(lifecycle, result)
            }
        }
    }

    private fun moveToDetailPage(id: String) {
        val toDetailFragment =
            HomeFragmentDirections.actionHomeFragmentToDetailStoryFragment(id)
        findNavController().navigate(toDetailFragment)
    }

    private fun moveToAddPage() {
        val toAddFragment = HomeFragmentDirections.actionHomeFragmentToAddStoryFragment()
        findNavController().navigate(toAddFragment)
    }
}