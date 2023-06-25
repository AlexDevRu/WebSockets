package com.example.learning_android_websockets_kulakov.ui.observable_coins

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.learning_android_websockets_kulakov.R
import com.example.learning_android_websockets_kulakov.databinding.FragmentObservableCoinsBinding
import com.example.learning_android_websockets_kulakov.models.Coin
import com.example.learning_android_websockets_kulakov.ui.adapters.ObservableCoinsAdapter
import com.example.learning_android_websockets_kulakov.ui.available_coins.AvailableCoinsFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ObservableCoinsFragment : Fragment(), View.OnClickListener, ObservableCoinsAdapter.Listener {

    private lateinit var binding: FragmentObservableCoinsBinding

    private val viewModel by viewModels<ObservableCoinsViewModel>()

    private val observableCoinsAdapter = ObservableCoinsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentObservableCoinsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnChoose.setOnClickListener(this)
        binding.rvObservableCoins.adapter = observableCoinsAdapter
        observe()
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.coins.collectLatest {
                    observableCoinsAdapter.submitList(it)
                }
            }
        }
    }

    override fun onClick(p0: View?) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, AvailableCoinsFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onItemRemove(coin: Coin) {

    }

}