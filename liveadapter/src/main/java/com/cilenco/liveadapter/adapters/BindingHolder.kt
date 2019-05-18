package com.cilenco.liveadapter.adapters

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BindingHolder<B: ViewDataBinding>(val binding: B): RecyclerView.ViewHolder(binding.root) {

    init {
        //itemView.setOnClickListener()
    }

}