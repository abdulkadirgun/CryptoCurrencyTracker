package com.example.cryptocurrencytracker.ui.common

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptocurrencytracker.data.source.local.db.entities.CoinEntity
import com.example.cryptocurrencytracker.databinding.CoinRowItemBinding
class HomeAdapter(
    private val coinList :ArrayList<CoinEntity>,
    private val mContext: Context,
    private val itemSelectionListener : (String) -> Unit
    ) : RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {

    inner class MyViewHolder(
        private val binding: CoinRowItemBinding,
        val onItemClicked: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root){

        init {
            itemView.setOnClickListener {
                onItemClicked(layoutPosition)
            }
        }
        fun bind(coin: CoinEntity) {
            binding.coinName.text = coin.name
            binding.coinSymbol.text = coin.symbol
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            CoinRowItemBinding.inflate(
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