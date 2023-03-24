package com.lucifergotmad.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lucifergotmad.storyapp.R
import com.lucifergotmad.storyapp.core.data.remote.request.LoginUserRequest
import com.lucifergotmad.storyapp.core.domain.User
import com.lucifergotmad.storyapp.core.helper.ViewModelFactory
import com.lucifergotmad.storyapp.databinding.FragmentLoginBinding
import kotlinx.coroutines.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupView()
        setupAction()
        setupAnimation()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_login -> {
                binding.apply {
                    if (edtEmail.text?.isNotEmpty() == true && edtPassword.text?.isNotEmpty() == true) {
                        val user = LoginUserRequest(
                            binding.edtEmail.text.toString(),
                            binding.edtPassword.text.toString()
                        )

                        viewModel.loginUser(user).observe(viewLifecycleOwner) { result ->
                            if (result != null) {
                                when (result) {
                                    is com.lucifergotmad.storyapp.core.data.Result.Loading -> {
                                        v.isEnabled = false
                                    }
                                    is com.lucifergotmad.storyapp.core.data.Result.Success -> {
                                        v.isEnabled = true
                                        CoroutineScope(Dispatchers.IO).launch {
                                            val response = result.data.loginResult
                                            val dataUser = User(
                                                response?.userId ?: "",
                                                response?.name ?: "",
                                                response?.token ?: "",
                                            )

                                            viewModel.saveUser(dataUser).join()

                                            withContext(Dispatchers.Main) {
                                                Toast.makeText(
                                                    context,
                                                    "Yeay! ${result.data.message}",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                moveToHomePage()
                                            }
                                        }
                                    }
                                    is com.lucifergotmad.storyapp.core.data.Result.Error -> {
                                        v.isEnabled = true
                                        Toast.makeText(
                                            context,
                                            "Somethings wrong! " + result.error,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }
                    } else {
                        if (edtEmail.text?.isEmpty() == true) {
                            edtEmail.error = getString(R.string.error_required_message, "Email")
                        }

                        if (edtPassword.text?.isEmpty() == true) {
                            edtPassword.error =
                                getString(R.string.error_required_message, "Password")
                            edtPassword.setCompoundDrawablesWithIntrinsicBounds(
                                null,
                                null,
                                null,
                                null
                            )
                        }
                    }
                }
            }
            R.id.btn_move_to_register -> {
                val toRegisterFragment =
                    LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                findNavController().navigate(toRegisterFragment)
            }
        }
    }

    private fun setupAction() {
        binding.btnMoveToRegister.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
    }

    private fun setupView() {
        val window = (requireActivity() as AppCompatActivity).window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            if (window.insetsController != null) {
                window.insetsController
                    ?.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                window.insetsController?.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        }
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(requireActivity(), factory)[LoginViewModel::class.java]
    }

    private fun moveToHomePage() {
        val toHomePage = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
        findNavController().navigate(toHomePage)
    }

    private fun setupAnimation() {
        binding.apply {
            ObjectAnimator.ofFloat(imageView, View.TRANSLATION_X, -30f, 30f).apply {
                duration = 6000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }.start()

            val title = ObjectAnimator.ofFloat(headerTitle, View.ALPHA, 1f).setDuration(500)
            val subTitle = ObjectAnimator.ofFloat(subTitle, View.ALPHA, 1f).setDuration(500)
            val emailTextView = ObjectAnimator.ofFloat(tvEmail, View.ALPHA, 1f).setDuration(500)
            val emailEditTextLayout =
                ObjectAnimator.ofFloat(edtEmailLayout, View.ALPHA, 1f).setDuration(500)
            val passwordTextView =
                ObjectAnimator.ofFloat(tvPassword, View.ALPHA, 1f).setDuration(500)
            val passwordEditTextLayout =
                ObjectAnimator.ofFloat(edtPasswordLayout, View.ALPHA, 1f).setDuration(500)
            val login = ObjectAnimator.ofFloat(btnLogin, View.ALPHA, 1f).setDuration(500)
            val moveToRegister =
                ObjectAnimator.ofFloat(btnMoveToRegister, View.ALPHA, 1f).setDuration(500)

            AnimatorSet().apply {
                playSequentially(
                    title,
                    subTitle,
                    emailTextView,
                    emailEditTextLayout,
                    passwordTextView,
                    passwordEditTextLayout,
                    login,
                    moveToRegister
                )
                startDelay = 500
            }.start()
        }

    }
}