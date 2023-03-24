package com.lucifergotmad.storyapp.ui.profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lucifergotmad.storyapp.core.helper.ViewModelFactory
import com.lucifergotmad.storyapp.databinding.FragmentProfileBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupView()
    }

    private fun setupView() {
        binding.btnLogout.setOnClickListener {
            viewModel.deleteUser()
        }
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(requireActivity(), factory)[ProfileViewModel::class.java]

        viewModel.getUser().observe(viewLifecycleOwner) { result ->
            if (result.token.isEmpty()) {
                val toLoginFragment =
                    ProfileFragmentDirections.actionProfileFragmentToLoginFragment()
                findNavController().navigate(toLoginFragment)
            }
        }
    }

}