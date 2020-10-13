package com.popstack.mvoter2015.rakhine.feature.party.search

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import coil.load
import com.popstack.mvoter2015.rakhine.R
import com.popstack.mvoter2015.rakhine.core.recyclerview.ViewBindingViewHolder
import com.popstack.mvoter2015.rakhine.databinding.ItemPartyBinding
import com.popstack.mvoter2015.domain.party.model.PartyId
import com.popstack.mvoter2015.rakhine.helper.diff.diffCallBackWith
import com.popstack.mvoter2015.rakhine.helper.extensions.inflater
import com.popstack.mvoter2015.rakhine.helper.extensions.withSafeAdapterPosition

internal class PartySearchPagingAdapter constructor(
  private val itemClick: (PartyId) -> Unit
) :
  PagingDataAdapter<PartySearchResultViewItem, PartySearchPagingAdapter.PartySearchResultViewItemViewHolder>(
    diffCallBackWith(
      areItemTheSame = { item1, item2 ->
        item1.partyId == item2.partyId
      },
      areContentsTheSame = { item1, item2 ->
        item1 == item2
      }
    )
  ) {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): PartySearchResultViewItemViewHolder {
    val binding = ItemPartyBinding.inflate(parent.inflater(), parent, false)
    val viewHolder = PartySearchResultViewItemViewHolder(binding)
    viewHolder.apply {
      itemView.setOnClickListener {
        withSafeAdapterPosition { position ->
          getItem(position)?.let { itemAtIndex ->
            itemClick.invoke(itemAtIndex.partyId)
          }
        }
      }
    }
    return viewHolder
  }

  override fun onBindViewHolder(
    holder: PartySearchResultViewItemViewHolder,
    position: Int
  ) {
    getItem(position)?.let { itemAtIndex ->
      holder.binding.apply {
        ivPartySeal.load(itemAtIndex.flagImageUrl) {
          placeholder(R.drawable.placeholder_rect)
          error(R.drawable.placeholder_rect)
          crossfade(true)
        }

        tvPartyName.text = itemAtIndex.name
        tvPartyRegion.text = itemAtIndex.region
      }
    }
  }

  class PartySearchResultViewItemViewHolder(
    binding: ItemPartyBinding
  ) : ViewBindingViewHolder<ItemPartyBinding>(binding)
}