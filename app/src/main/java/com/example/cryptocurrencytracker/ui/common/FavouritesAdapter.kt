package com.example.cryptocurrencytracker.ui.common

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptocurrencytracker.databinding.CoinDetailRowItemBinding
import com.example.cryptocurrencytracker.domain.model.CoinDetailItem
import com.example.cryptocurrencytracker.domain.model.CoinItem

class FavouritesAdapter(
    private val coinList :ArrayList<CoinItem>,
    private val mContext: Context,
    private val itemSelectionListener : (String) -> Unit
    ) : RecyclerView.Adapter<FavouritesAdapter.MyViewHolder>() {

    inner class MyViewHolder(
        private val binding: CoinDetailRowItemBinding,
        val onItemClicked: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root){

        init {
            itemView.setOnClickListener {
                onItemClicked(layoutPosition)
            }
        }
        fun bind(coin: CoinItem) {
            Glide.with(mContext).load(coin.image).into(binding.coinImage)
            binding.coinName.text = coin.name
            binding.coinSymbol.text = coin.symbol
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            CoinDetailRowItemBinding.inflate(
                LayoutInflater.from(mContext),
                parent,
                false
            )
        ){
            itemSelectionListener(coinList[it].id)
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(coinList[position])
    }

    override fun getItemCount() = coinList.size



}