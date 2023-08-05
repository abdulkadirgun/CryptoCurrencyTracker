package com.example.cryptocurrencytracker.ui.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.cryptocurrencytracker.databinding.FragmentRegisterBinding
import com.example.cryptocurrencytracker.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
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

        binding.registerBtn.setOnClickListener {
            //Log.d("burdayım", "registerBtn clicked")
            viewModel.registerUser("testtest1@gmail.com","testtest1")
        }

        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.registerInfo.collect{ result->
                    result?.let {
                        when(result){
                            is Resource.Error -> {
                                Log.d("RegisterFragment", "registerInfo: Error${result.message}")
                            }
                            is Resource.Loading -> {
                                Log.d("RegisterFragment", "registerInfo: Loading")
                            }
                            is Resource.Success -> {
                                Log.d("RegisterFragment", "registerInfo: Success${result.data?.user?.email}")
                                val email = result.data?.user?.email
                                findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment(email!!))
                            }
                        }
                    }

                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}