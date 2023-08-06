package com.example.cryptocurrencytracker.ui.screens.register

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
import com.example.cryptocurrencytracker.databinding.FragmentRegisterBinding
import com.example.cryptocurrencytracker.util.Resource
import com.example.cryptocurrencytracker.util.hide
import com.example.cryptocurrencytracker.util.isEmailValid
import com.example.cryptocurrencytracker.util.show
import com.example.cryptocurrencytracker.util.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment() {


    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClicks()

        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.registerInfo.collect{ result->
                    result?.let {
                        when(result){
                            is Resource.Error -> {
                                Log.d("RegisterFragment", "registerInfo: Error${result.message}")
                                binding.progressBar.hide()
                                binding.mainContainer.show()
                                result.message?.showToast(requireContext())
                            }
                            is Resource.Loading -> {
                                Log.d("RegisterFragment", "registerInfo: Loading")
                                binding.mainContainer.hide()
                                binding.progressBar.show()
                            }
                            is Resource.Success -> {
                                Log.d("RegisterFragment", "registerInfo: Success${result.data?.user?.email}")
                                binding.progressBar.hide()
                                binding.mainContainer.show()
                                "registered successfully".showToast(requireContext())
                                findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setClicks() {
        binding.registerBtn.setOnClickListener {
            if (binding.email.isEmailValid())
                viewModel.registerUser(binding.email.text.toString(),binding.password.text.toString())
            else
                Toast.makeText(requireContext(), "mail is not valid", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}