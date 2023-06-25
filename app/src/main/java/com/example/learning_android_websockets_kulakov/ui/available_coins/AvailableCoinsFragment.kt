package com.example.learning_android_websockets_kulakov.ui.available_coins

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.learning_android_websockets_kulakov.databinding.FragmentAllCoinsBinding
import com.example.learning_android_websockets_kulakov.models.Coin
import com.example.learning_android_websockets_kulakov.ui.adapters.AvailableCoinsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AvailableCoinsFragment : Fragment(), AvailableCoinsAdapter.Listener {

    private lateinit var binding: FragmentAllCoinsBinding

    private val viewModel by viewModels<AvailableCoinsViewModel>()

    private val availableCoinsAdapter = AvailableCoinsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllCoinsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvCoins.adapter = availableCoinsAdapter
        observe()
    }

    private fun observe() {
        viewModel.coins.observe(viewLifecycleOwner) {
            availableCoinsAdapter.submitList(it)
        }
        viewModel.loading.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.finish.collectLatest {
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        }
    }

    override fun onItemClick(coin: Coin) {
        viewModel.insertCoin(coin)
    }

}