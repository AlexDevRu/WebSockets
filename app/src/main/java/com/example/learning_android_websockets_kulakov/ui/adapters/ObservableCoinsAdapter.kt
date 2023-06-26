package com.example.learning_android_websockets_kulakov.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.learning_android_websockets_kulakov.R
import com.example.learning_android_websockets_kulakov.databinding.ItemObservableCoinBinding
import com.example.learning_android_websockets_kulakov.models.Coin

class ObservableCoinsAdapter(
    private val listener: Listener
): ListAdapter<Coin, ObservableCoinsAdapter.CoinViewHolder>(DIFF_UTIL) {

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Coin>() {
            override fun areItemsTheSame(oldItem: Coin, newItem: Coin): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Coin, newItem: Coin): Boolean {
                return oldItem == newItem
            }

            override fun getChangePayload(oldItem: Coin, newItem: Coin): Any? {
                return if (oldItem.priceUsd != newItem.priceUsd) PRICE_PAYLOAD else null
            }
        }

        private const val PRICE_PAYLOAD = 1
    }

    interface Listener {
        fun onItemRemove(coin: Coin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val binding = ItemObservableCoinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoinViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: CoinViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.firstOrNull() == PRICE_PAYLOAD) {
            holder.bindPrice(getItem(position))
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    inner class CoinViewHolder(
        private val binding: ItemObservableCoinBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private var coin: Coin? = null

        init {
            binding.btnRemove.setOnClickListener(this)
        }

        fun bind(coin: Coin) {
            this.coin = coin
            binding.tvCurrency.text = coin.name
            binding.tvSymbol.text = coin.symbol
            Glide.with(binding.ivCurrency)
                .load(coin.getImageUrl())
                .into(binding.ivCurrency)
            bindPrice(coin)
        }

        fun bindPrice(coin: Coin) {
            binding.tvPrice.text = binding.root.context.getString(R.string.price, coin.priceUsd.toFloat())
        }

        override fun onClick(view: View?) {
            when (view) {
                binding.btnRemove -> coin?.let { listener.onItemRemove(it) }
            }
        }
    }
}
