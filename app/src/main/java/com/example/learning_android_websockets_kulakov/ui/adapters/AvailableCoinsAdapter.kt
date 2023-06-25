package com.example.learning_android_websockets_kulakov.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.learning_android_websockets_kulakov.databinding.ItemCoinBinding
import com.example.learning_android_websockets_kulakov.models.Coin

class AvailableCoinsAdapter(
    private val listener: Listener
): ListAdapter<Coin, AvailableCoinsAdapter.CoinViewHolder>(DIFF_UTIL) {

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Coin>() {
            override fun areItemsTheSame(oldItem: Coin, newItem: Coin): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Coin, newItem: Coin): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface Listener {
        fun onItemClick(coin: Coin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val binding = ItemCoinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoinViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CoinViewHolder(
        private val binding: ItemCoinBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        private var coin: Coin? = null

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(coin: Coin) {
            this.coin = coin
            binding.tvCurrency.text = coin.name
            Glide.with(binding.ivCurrency)
                .load(coin.getImageUrl())
                .into(binding.ivCurrency)
            binding.tvPrice.text = "${coin.priceUsd}$"
        }

        override fun onClick(view: View?) {
            when (view) {
                binding.root -> coin?.let { listener.onItemClick(it) }
            }
        }
    }
}