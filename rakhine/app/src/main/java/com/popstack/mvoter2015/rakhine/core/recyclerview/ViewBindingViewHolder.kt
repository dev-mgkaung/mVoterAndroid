package com.popstack.mvoter2015.rakhine.core.recyclerview

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding

abstract class ViewBindingViewHolder<out VB : ViewBinding>(
  val binding: VB
) : ViewHolder(binding.root)