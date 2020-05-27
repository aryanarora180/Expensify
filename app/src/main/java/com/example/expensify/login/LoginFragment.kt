package com.example.expensify.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.expensify.R
import com.example.expensify.databinding.FragmentLoginBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    companion object {
        const val TAG = "LoginFragment"
        const val SIGN_IN_REQUEST_CODE = 1001
        const val SIGN_IN_RESULT_CODE = 1001
    }

    private val viewModel by viewModels<LoginViewModel>()

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeAuthenticationState()
    }

    private fun observeAuthenticationState() {
        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                    findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
                }
                else -> {
                    binding.authButton.visibility = View.VISIBLE
                    binding.authButton.setOnClickListener { launchSignInFlow() }
                }
            }
        })
    }

    private fun launchSignInFlow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            SIGN_IN_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_RESULT_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                Log.i(
                    TAG,
                    "Successfully signed in user " + "${FirebaseAuth.getInstance().currentUser?.displayName}!"
                )
                findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
            } else {
                Log.e(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
                if (response != null)
                    Snackbar.make(
                        binding.loginCoordinator,
                        R.string.login_failed,
                        Snackbar.LENGTH_SHORT
                    )
            }
        }
    }

}
