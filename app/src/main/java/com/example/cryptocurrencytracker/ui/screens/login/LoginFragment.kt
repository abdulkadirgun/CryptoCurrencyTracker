package com.example.cryptocurrencytracker.ui.screens.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.cryptocurrencytracker.databinding.FragmentLoginBinding
import com.example.cryptocurrencytracker.util.Resource
import com.example.cryptocurrencytracker.util.gone
import com.example.cryptocurrencytracker.util.isEmailValid
import com.example.cryptocurrencytracker.util.show
import com.example.cryptocurrencytracker.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    private val emailFromRegisterPage by lazy { LoginFragmentArgs.fromBundle(requireArguments()).email }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClicks()

        if (emailFromRegisterPage != " ")
            binding.email.setText(emailFromRegisterPage)

        viewModel.checkUserLoggedIn()

        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.isUserAlsoLoggedIn.collect{ result->
                    result?.let {
                        when(result){
                            is Resource.Error -> {
                                Log.d("LoginFragment", "loginInfo: Error${result.message}")
                                binding.progressBar.gone()
                                binding.mainContainer.show()
                                result.message?.showToast(requireContext())
                            }
                            is Resource.Loading -> {
                                Log.d("LoginFragment", "loginInfo: Loading")
                                binding.mainContainer.gone()
                                binding.progressBar.show()
                            }
                            is Resource.Success -> {
                                if (result.data!! && emailFromRegisterPage == " "){
                                    goToHomePage()
                                    "user already logged in".showToast(requireContext())
                                }
                                else
                                    viewModel.loginInfo.collect{ result->
                                        result?.let {
                                            when(result){
                                                is Resource.Error -> {
                                                    Log.d("LoginFragment", "loginInfo: Error${result.message}")
                                                    binding.progressBar.gone()
                                                    binding.mainContainer.show()
                                                    result.message?.showToast(requireContext())
                                                }
                                                is Resource.Loading -> {
                                                    Log.d("LoginFragment", "loginInfo: Loading")
                                                    binding.mainContainer.gone()
                                                    binding.progressBar.show()
                                                }
                                                is Resource.Success -> {
                                                    goToHomePage()
                                                    "logged in successfully ".showToast(requireContext())
                                                }
                                            }
                                        }
                                    }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun goToHomePage(){
        binding.progressBar.gone()
        binding.mainContainer.show()
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
    }

    private fun setClicks() {

        binding.apply {
            loginBtn.setOnClickListener {
                if (binding.email.isEmailValid())
                    viewModel.loginUser(binding.email.text.toString(),binding.password.text.toString())
                else
                    Toast.makeText(requireContext(), "mail is not valid", Toast.LENGTH_SHORT).show()
            }

            registerNow.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}